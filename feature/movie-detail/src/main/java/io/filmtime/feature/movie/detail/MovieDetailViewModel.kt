package io.filmtime.feature.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.stream.GetStreamInfoUseCase
import io.filmtime.domain.tmdb.movies.GetMovieCreditsUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.tmdb.movies.GetSimilarUseCase
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
  private val getCredit: GetMovieCreditsUseCase,
  private val getSimilar: GetSimilarUseCase,
  private val addBookmark: AddBookmarkUseCase,
  private val deleteBookmark: DeleteBookmarkUseCase,
  private val observeBookmark: ObserveBookmarkUseCase,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")

  private val _state: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState())
  val state = _state.asStateFlow()

  private val _similarState: MutableStateFlow<MovieDetailSimilarState> = MutableStateFlow(MovieDetailSimilarState())
  val similarState = _similarState.asStateFlow()

  private val _creditState: MutableStateFlow<MovieDetailCreditState> = MutableStateFlow(MovieDetailCreditState())
  val creditState = _creditState.asStateFlow()

  val navigateToPlayer = MutableSharedFlow<String?>()

  init {
    load()
    loadCredits()
    loadSimilar()
    observeBookmark()
  }

  fun load() = viewModelScope.launch {
    _state.value = _state.value.copy(isLoading = true, error = null)

    getMovieDetail(videoId).collect {
      when (val result = it) {
        is Success -> {
          _state.update { state ->
            state.copy(videoDetail = result.data, isLoading = false, error = null)
          }
        }

        is Failure -> {
          _state.update { state ->
            state.copy(error = result.error.toUiMessage(), isLoading = false)
          }
        }
      }
    }
  }

  fun loadSimilar() = viewModelScope.launch {
    _similarState.value = _similarState.value.copy(isLoading = true, error = null)

    when (val result = getSimilar(videoId)) {
      is Success -> {
        _similarState.update { state ->
          state.copy(videoItems = result.data, isLoading = false, error = null)
        }
      }

      is Failure -> {
        _similarState.update { state ->
          state.copy(error = result.error.toUiMessage(), isLoading = false)
        }
      }
    }
  }

  fun loadCredits() = viewModelScope.launch {
    _creditState.value = _creditState.value.copy(isLoading = true, error = null)

    when (val result = getCredit(videoId)) {
      is Success -> {
        _creditState.update { state ->
          state.copy(credit = result.data, isLoading = false, error = null)
        }
      }

      is Failure -> {
        _creditState.update { state ->
          state.copy(error = result.error.toUiMessage(), isLoading = false)
        }
      }
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
