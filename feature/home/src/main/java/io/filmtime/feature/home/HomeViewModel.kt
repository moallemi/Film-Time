package io.filmtime.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoListType
import io.filmtime.domain.tmdb.movies.GetBookmarkedMoviesUseCase
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import io.filmtime.domain.tmdb.shows.GetBookmarkedShowsUseCase
import io.filmtime.domain.tmdb.shows.GetTrendingShowsUseCase
import io.filmtime.feature.home.HomeAction.Reload
import io.filmtime.feature.home.SectionType.None
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
  private val getMoviesList: GetMoviesListUseCase,
  private val getTrendingShows: GetTrendingShowsUseCase,
  private val getBookmarkedMoviesUseCase: GetBookmarkedMoviesUseCase,
  private val getBookmarkedShowsUseCase: GetBookmarkedShowsUseCase,
) : ViewModel() {

  private val pendingActions = MutableSharedFlow<HomeAction>()

  private val _state = MutableStateFlow(HomeUiState())
  val state = _state.asStateFlow()

  init {
    collectActions()
    load()
  }

  fun submitAction(action: HomeAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        Reload -> reload()
      }
    }
  }

  private fun load() {
    launch {
      loadTrendingMovies()
      loadTrendingShows()
    }

    launch {
      loadBookmarkedShows()
    }
    launch {
      loadBookmarkedMovies()
    }
  }

  private fun reload() {
    _state.update { state -> state.copy(error = null, videoSections = emptyList()) }
    load()
  }

  private suspend fun loadTrendingMovies() {
    _state.update { state -> state.copy(isLoading = true) }
    getMoviesList(
      videoListType = VideoListType.Trending,
    )
      .onCompletion { _state.update { state -> state.copy(isLoading = false) } }
      .onEach { result ->
        when (result) {
          is Success -> {
            _state.update { state ->
              state.copy(
                videoSections = state.videoSections + listOf(
                  VideoSection(
                    title = SectionType.TrendingMovies.title,
                    items = result.data,
                    type = SectionType.TrendingMovies,
                  ),
                ),
              )
            }
          }

          is Failure -> _state.update { state ->
            state.copy(
              error = result.error.toUiMessage(),
              isLoading = false,
            )
          }
        }
      }
      .collect()
  }

  private suspend fun loadTrendingShows() {
    _state.update { state -> state.copy(isLoading = true) }
    getTrendingShows()
      .onCompletion { _state.update { state -> state.copy(isLoading = false) } }
      .onEach { result ->
        when (result) {
          is Success -> {
            _state.update { state ->
              state.copy(
                videoSections = state.videoSections + listOf(
                  VideoSection(
                    title = SectionType.TrendingShows.title,
                    items = result.data,
                    type = SectionType.TrendingShows,
                  ),
                ),
              )
            }
          }

          is Failure -> _state.update { state ->
            state.copy(
              error = result.error.toUiMessage(),
              isLoading = false,
            )
          }
        }
      }
      .collect()
  }

  private suspend fun loadBookmarkedMovies() {
    getBookmarkedMoviesUseCase()
      .onEach { result ->
        _state.update { state ->
          state.copy(
            bookmarkedMovies = if (result.isEmpty()) {
              null
            } else {
              VideoSection(
                type = None,
                title = "Bookmarked Movies",
                items = result,
              )
            },
          )
        }
      }.collect()
  }

  private suspend fun loadBookmarkedShows() {
    getBookmarkedShowsUseCase()
      .onEach { result ->
        _state.update { state ->
          state.copy(
            bookmarkedShows = if (result.isEmpty()) {
              null
            } else {
              VideoSection(
                type = None,
                title = "Bookmarked Shows",
                items = result,
              )
            },
          )
        }
      }.collect()
  }
}
