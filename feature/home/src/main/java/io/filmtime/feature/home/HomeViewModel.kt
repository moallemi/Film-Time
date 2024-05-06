package io.filmtime.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoListType
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import io.filmtime.domain.tmdb.shows.GetTrendingShowsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
  private val getMoviesList: GetMoviesListUseCase,
  private val getTrendingShows: GetTrendingShowsUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow(HomeUiState(isLoading = false))
  val state = _state.asStateFlow()

  init {
    load()
  }

  private fun load() {
    viewModelScope.launch {
      loadTrendingMovies()
      loadTrendingShows()
    }
  }

  fun reload() {
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
                    title = "Trending Movies",
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
                    title = "Trending Shows",
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
}
