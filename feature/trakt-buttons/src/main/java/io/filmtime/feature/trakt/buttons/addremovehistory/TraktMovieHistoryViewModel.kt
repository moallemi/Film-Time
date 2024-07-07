package io.filmtime.feature.trakt.buttons.addremovehistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType
import io.filmtime.domain.trakt.history.AddMovieToHistoryUseCase
import io.filmtime.domain.trakt.history.IsMovieWatchedUseCase
import io.filmtime.domain.trakt.history.RemoveMovieFromHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TraktMovieHistoryViewModel @Inject constructor(
  private val isMovieWatchedUseCase: IsMovieWatchedUseCase,
  private val addToHistory: AddMovieToHistoryUseCase,
  private val removeFromHistory: RemoveMovieFromHistoryUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow(TraktAddRemoveUiState())
  val state = _state.asStateFlow()

  fun checkIfIsWatched(videoType: VideoType, tmdbId: Int) = viewModelScope.launch {
    _state.update { state -> state.copy(isLoading = true) }

    when (val result = isMovieWatchedUseCase(tmdbId)) {
      is Success -> _state.update { state ->
        state.copy(isWatched = result.data.isWatched, isLoading = false, traktId = result.data.traktId)
      }

      is Failure -> _state.update { state -> state.copy(isLoading = false, isError = true) }
    }
  }

  fun addItemToHistory() = viewModelScope.launch {
    val traktId = state.value.traktId ?: return@launch

    _state.update { state -> state.copy(isLoading = true) }

    when (addToHistory(traktId)) {
      is Success -> _state.update { state -> state.copy(isWatched = true, isLoading = false) }
      is Failure -> _state.update { state -> state.copy(isLoading = false) }
    }
  }

  fun removeItemFromHistory() = viewModelScope.launch {
    val traktId = state.value.traktId ?: return@launch

    _state.update { state -> state.copy(isLoading = true) }

    when (removeFromHistory(traktId)) {
      is Success -> _state.update { state -> state.copy(isWatched = false, isLoading = false) }
      is Failure -> _state.update { state -> state.copy(isLoading = false) }
    }
  }
}
