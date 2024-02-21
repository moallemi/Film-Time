package io.filmtime.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoListType
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import io.filmtime.domain.tmdb.shows.GetTrendingShowsUseCase
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import io.filmtime.domain.trakt.auth.TraktAuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
  private val getMoviesList: GetMoviesListUseCase,
  private val getTrendingShows: GetTrendingShowsUseCase,
  private val getTraktAuthStateUseCase: GetTraktAuthStateUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow(HomeUiState(isLoading = false))
  val state = _state.asStateFlow()

  private val _traktAuthState: MutableStateFlow<TraktAuthState> = MutableStateFlow(value = TraktAuthState.SignedOut)
  val traktState = _traktAuthState.asStateFlow()

  init {
    viewModelScope.launch {
      loadTrendingMovies()
      loadTrendingShows()
      traktState()
    }
  }

  private suspend fun traktState() {
    getTraktAuthStateUseCase().collect {
      _traktAuthState.value = it
    }
  }

  private suspend fun loadTrendingMovies() {
    getMoviesList(
      videoListType = VideoListType.Trending,
    )
      .onStart {
        _state.update { state -> state.copy(isLoading = true) }
      }
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

          is Failure -> {
            Log.e("loadTrendingMovies", result.error.toString())
            // TODO: Handle error
            result.error
          }
        }
      }
      .collect()
  }

  private suspend fun loadTrendingShows() {
    getTrendingShows()
      .onStart {
        _state.update { state -> state.copy(isLoading = true) }
      }
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

          is Failure -> {
            // TODO: Handle error
          }
        }
      }
      .collect()
  }
}
