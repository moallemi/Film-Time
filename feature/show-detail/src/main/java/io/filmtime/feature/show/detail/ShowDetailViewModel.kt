package io.filmtime.feature.show.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.domain.tmdb.shows.GetShowDetailsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getShowDetails: GetShowDetailsUseCase,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")
  private val _state: MutableStateFlow<ShowDetailState> = MutableStateFlow(ShowDetailState())
  val state = _state.asStateFlow()

  val navigateToPlayer = MutableSharedFlow<String?>()

  init {
    load()
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
