package io.filmtime.feature.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.stream.GetStreamInfoUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMovieDetail: GetMovieDetailsUseCase,
  private val getStreamInfo: GetStreamInfoUseCase,
  private val addBookmark: AddBookmarkUseCase,
  private val deleteBookmark: DeleteBookmarkUseCase,
  private val observeBookmark: ObserveBookmarkUseCase,
  private val getRatings: GetRatingsUseCase,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")

  private val _state = MutableStateFlow(MovieDetailState())
  val state = _state.asStateFlow()

  val navigateToPlayer = MutableSharedFlow<String?>()

  init {
    loadMovieDetail()
    observeBookmark()
  }

  fun loadMovieDetail() = viewModelScope.launch {
    _state.value = _state.value.copy(isLoading = true, error = null)

    getMovieDetail(videoId)
      .collect { result ->
        result.fold(
          onSuccess = { data ->
            _state.update { state -> state.copy(videoDetail = data, isLoading = false) }
            loadRatings()
          },
          onFailure = { e -> _state.update { state -> state.copy(isLoading = false, error = e.toUiMessage()) } },
        )
      }
  }

  private fun loadRatings() = viewModelScope.launch {
    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      getRatings(type = Movie, tmdbId = tmdbId)
        .fold(
          onSuccess = { ratings -> _state.update { state -> state.copy(ratings = ratings) } },
          onFailure = { error -> _state.update { state -> state.copy(error = error.toUiMessage()) } },
        )
    }
  }

  fun loadStreamInfo() = viewModelScope.launch {
    _state.value = _state.value.copy(isStreamLoading = true)
    getStreamInfo()
      .onEach { streamInfo ->
        _state.value = _state.value.copy(streamInfo = streamInfo, isStreamLoading = false)
        navigateToPlayer.emit(streamInfo.url)
      }
      .collect()
  }

  private fun observeBookmark() = viewModelScope.launch {
    observeBookmark(videoId, Movie)
      .onEach { isBookmarked ->
        _state.update { state ->
          state.copy(isBookmarked = isBookmarked)
        }
      }
      .collect()
  }

  fun addBookmark() = viewModelScope.launch {
    addBookmark(videoId, Movie)
  }

  fun removeBookmark() = viewModelScope.launch {
    deleteBookmark(videoId, Movie)
  }
}
