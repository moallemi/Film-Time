package io.filmtime.feature.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.trakt.TraktHistoryRepository
import io.filmtime.domain.stream.GetStreamInfoUseCase
import io.filmtime.domain.tmdb.movies.GetMovieCreditUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMovieDetail: GetMovieDetailsUseCase,
  private val getStreamInfo: GetStreamInfoUseCase,
  private val getCredit: GetMovieCreditUseCase,
  private val traktHistoryRepository: TraktHistoryRepository,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")
  private val _state: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState())
  val state = _state.asStateFlow()
  private val _similarState: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState())
  val similarState = _similarState.asStateFlow()

  private val _creditState: MutableStateFlow<MovieDetailCreditState> = MutableStateFlow(MovieDetailCreditState())
  val creditState = _creditState.asStateFlow()

  val navigateToPlayer = MutableSharedFlow<String?>()

  init {
    load()
    loadSimilar()
    loadCredits()
  }

  private fun loadSimilar() = viewModelScope.launch {
  }

  private fun loadCredits() = viewModelScope.launch {
    _creditState.value = _creditState.value.copy(isLoading = true)
    when (val result = getCredit(videoId)) {
      is Failure -> {
        when (result.error) {

          is GeneralError.ApiError -> {
            _state.value = _state.value.copy(
              error = result.error,
              message = (result.error as GeneralError.ApiError).message,
              isLoading = false,
            )
          }

          GeneralError.NetworkError -> {
            _state.value = _state.value.copy(
              error = result.error,
              message = "No internet connection. Please check your network settings.",
              isLoading = false,
            )
          }

          is GeneralError.UnknownError -> _state.value = _state.value.copy(
            error = result.error,
            message = (result.error as GeneralError.UnknownError).error.message,
            isLoading = false,
          )
        }
      }

      is Success -> {
        _creditState.value = _creditState.value.copy(credit = result.data, isLoading = false)
      }
    }

  }

  fun load() = viewModelScope.launch {
    _state.value = _state.value.copy(isLoading = true)

    when (val result = getMovieDetail(videoId)) {
      is Success -> {
        _state.value = _state.value.copy(videoDetail = result.data, isLoading = false)
      }

      is Failure -> {
        when (result.error) {
          is GeneralError.ApiError -> {
            _state.value = _state.value.copy(
              error = result.error,
              message = (result.error as GeneralError.ApiError).message,
              isLoading = false,
            )
          }

          GeneralError.NetworkError -> {
            _state.value = _state.value.copy(
              error = result.error,
              message = "No internet connection. Please check your network settings.",
              isLoading = false,
            )
          }

          is GeneralError.UnknownError -> _state.value = _state.value.copy(
            error = result.error,
            message = (result.error as GeneralError.UnknownError).error.message,
            isLoading = false,
          )
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

  fun addItemToHistory() = viewModelScope.launch {
    val traktId = _state.value.videoDetail?.ids?.traktId ?: return@launch
    val result = traktHistoryRepository.addToHistory(traktId.toString())
    when (result) {
      is Failure -> TODO()
      is Success -> {
        val updated = _state.value.videoDetail?.copy(isWatched = true)
        _state.value = _state.value.copy(videoDetail = updated)
      }
    }
  }
}
