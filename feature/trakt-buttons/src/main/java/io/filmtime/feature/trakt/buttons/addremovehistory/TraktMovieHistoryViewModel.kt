package io.filmtime.feature.trakt.buttons.addremovehistory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType
import io.filmtime.domain.trakt.history.AddMovieToHistoryUseCase
import io.filmtime.domain.trakt.history.IsMovieWatchedUseCase
import io.filmtime.domain.trakt.history.RemoveMovieFromHistoryUseCase
import io.filmtime.feature.trakt.buttons.addremovehistory.TraktAddRemoveAction.AddToHistory
import io.filmtime.feature.trakt.buttons.addremovehistory.TraktAddRemoveAction.CheckIfWatched
import io.filmtime.feature.trakt.buttons.addremovehistory.TraktAddRemoveAction.RemoveFromHistory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class TraktMovieHistoryViewModel @Inject constructor(
  private val isMovieWatchedUseCase: IsMovieWatchedUseCase,
  private val addToHistory: AddMovieToHistoryUseCase,
  private val removeFromHistory: RemoveMovieFromHistoryUseCase,
) : ViewModel() {

  private val pendingActions = MutableSharedFlow<TraktAddRemoveAction>()

  private val _state = MutableStateFlow(TraktAddRemoveUiState())
  val state = _state.asStateFlow()

  init {
    collectActions()
  }

  fun submitAction(action: TraktAddRemoveAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is CheckIfWatched -> checkIfIsWatched(action.videoType, action.tmdbId)
        is AddToHistory -> addItemToHistory()
        is RemoveFromHistory -> removeItemFromHistory()
      }
    }
  }

  private fun checkIfIsWatched(videoType: VideoType, tmdbId: Int) = launch {
    _state.update { state -> state.copy(isLoading = true) }

    when (val result = isMovieWatchedUseCase(tmdbId)) {
      is Success -> _state.update { state ->
        state.copy(isWatched = result.data.isWatched, isLoading = false, traktId = result.data.traktId)
      }

      is Failure -> _state.update { state -> state.copy(isLoading = false, isError = true) }
    }
  }

  private fun addItemToHistory() = launch {
    val traktId = state.value.traktId ?: return@launch

    _state.update { state -> state.copy(isLoading = true) }

    when (addToHistory(traktId)) {
      is Success -> _state.update { state -> state.copy(isWatched = true, isLoading = false) }
      is Failure -> _state.update { state -> state.copy(isLoading = false) }
    }
  }

  private fun removeItemFromHistory() = launch {
    val traktId = state.value.traktId ?: return@launch

    _state.update { state -> state.copy(isLoading = true) }

    when (removeFromHistory(traktId)) {
      is Success -> _state.update { state -> state.copy(isWatched = false, isLoading = false) }
      is Failure -> _state.update { state -> state.copy(isLoading = false) }
    }
  }
}
