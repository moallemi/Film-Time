package io.filmtime.feature.movies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoListType
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
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
internal class MoviesViewModel @Inject constructor(
  private val getMoviesList: GetMoviesListUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow(MoviesUiState(isLoading = false))
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch {
      loadMoviesSection(VideoListType.Trending)
      loadMoviesSection(VideoListType.NowPlaying)
      loadMoviesSection(VideoListType.Popular)
      loadMoviesSection(VideoListType.TopRated)
      loadMoviesSection(VideoListType.Upcoming)
    }
  }

  private suspend fun loadMoviesSection(videoListType: VideoListType) {
    getMoviesList(
      videoListType = videoListType,
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
                    title = videoListType.name,
                    items = result.data,
                    type = videoListType,
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
}
