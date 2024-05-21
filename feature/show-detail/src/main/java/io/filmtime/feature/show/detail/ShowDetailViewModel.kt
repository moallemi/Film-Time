package io.filmtime.feature.show.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.domain.stream.GetStreamInfoUseCase
import io.filmtime.domain.tmdb.shows.GetShowCreditUseCase
import io.filmtime.domain.tmdb.shows.GetShowDetailsUseCase
import io.filmtime.domain.tmdb.shows.GetSimilarUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getShowDetails: GetShowDetailsUseCase,
  private val getShowCreditUseCase: GetShowCreditUseCase,
  private val getSimilarUseCase: GetSimilarUseCase,
  private val getStreamInfo: GetStreamInfoUseCase,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")
  private val _state: MutableStateFlow<ShowDetailState> = MutableStateFlow(ShowDetailState())
  val state = _state.asStateFlow()
  private val _similarState: MutableStateFlow<ShowDetailSimilarState> = MutableStateFlow(ShowDetailSimilarState())
  val similarState = _similarState.asStateFlow()

  private val _creditState: MutableStateFlow<ShowDetailCreditState> = MutableStateFlow(ShowDetailCreditState())
  val creditState = _creditState.asStateFlow()


  init {
    load()
    loadSimilar()
    loadCredits()
  }
  private fun loadSimilar() = viewModelScope.launch {
    _similarState.value = _similarState.value.copy(isLoading = true)
    when (val result = getSimilarUseCase(videoId)) {
      is Failure -> {
        when (result.error) {
          is GeneralError.ApiError -> {
            _similarState.update { state ->
              state.copy(
                errorMessage = (result.error as GeneralError.ApiError).message.orEmpty(),
                isLoading = false,
              )
            }
          }

          GeneralError.NetworkError -> {
            _similarState.update { state ->
              state.copy(
                errorMessage = "No internet connection. Please check your network settings.",
                isLoading = false,
              )
            }
          }

          is GeneralError.UnknownError -> _similarState.update { state ->
            state.copy(
              errorMessage = (result.error as GeneralError.UnknownError).error.message.orEmpty(),
              isLoading = false,
            )
          }
        }
      }

      is Success -> {
        _similarState.update { state ->
          state.copy(videoItems = result.data, isLoading = false, errorMessage = "")
        }
      }
    }
  }

  private fun loadCredits() = viewModelScope.launch {
    _creditState.value = _creditState.value.copy(isLoading = true)
    when (val result = getShowCreditUseCase(videoId)) {
      is Failure -> {
        when (result.error) {
          is GeneralError.ApiError -> {
            _creditState.update { state ->
              state.copy(
                error = result.error,
                message = (result.error as GeneralError.ApiError).message,
                isLoading = false,
              )
            }
          }

          GeneralError.NetworkError -> {
            _creditState.update { state ->
              state.copy(
                error = result.error,
                message = "No internet connection. Please check your network settings.",
                isLoading = false,
              )
            }
          }

          is GeneralError.UnknownError -> _creditState.update { state ->
            state.copy(
              error = result.error,
              message = (result.error as GeneralError.UnknownError).error.message,
              isLoading = false,
            )
          }
        }
      }

      is Success -> {
        _creditState.update { state ->
          state.copy(credit = result.data, isLoading = false)
        }
      }
    }
  }

  fun load() = viewModelScope.launch {
    _state.value = _state.value.copy(isLoading = true)

    when (val result = getShowDetails(videoId)) {
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

}
