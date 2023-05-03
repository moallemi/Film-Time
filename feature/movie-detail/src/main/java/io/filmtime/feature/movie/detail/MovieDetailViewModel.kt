package io.filmtime.feature.movie.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
  private val getMovieDetail: GetMovieDetailsUseCase,
) : ViewModel() {

  private val _state: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState())
  val state = _state.asStateFlow()

  init {
    load(550)
  }

  fun load(videoId: Int) = viewModelScope.launch {
    _state.value = _state.value.copy(isLoading = true)
    val result = getMovieDetail(videoId)
    _state.value = _state.value.copy(videoThumbnail = result, isLoading = false)
  }
}
