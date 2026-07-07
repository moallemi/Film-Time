package io.filmtime.feature.credits

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType
import io.filmtime.domain.tmdb.movies.GetMovieCreditsUseCase
import io.filmtime.domain.tmdb.shows.GetShowCreditsUseCase
import io.filmtime.feature.credits.CreditsAction.LoadCredits
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class CreditsViewModel @Inject constructor(
  private val getMovieCredits: GetMovieCreditsUseCase,
  private val getShowCredits: GetShowCreditsUseCase,
) : ViewModel() {

  private val pendingActions = MutableSharedFlow<CreditsAction>()

  private val _creditState = MutableStateFlow(CreditsUiState())
  val state = _creditState.asStateFlow()

  init {
    collectActions()
  }

  fun submitAction(action: CreditsAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is LoadCredits -> loadCredits(action.videoId, action.videoType)
      }
    }
  }

  private fun loadCredits(
    videoId: Int,
    videoType: VideoType,
  ) = launch {
    _creditState.value = _creditState.value.copy(isLoading = true, error = null)

    val result = if (videoType == VideoType.Movie) {
      getMovieCredits(videoId)
    } else {
      getShowCredits(videoId)
    }

    when (result) {
      is Success -> _creditState.update { state ->
        state.copy(credit = result.data, isLoading = false, error = null)
      }

      is Failure -> _creditState.update { state ->
        state.copy(error = result.error.toUiMessage(), isLoading = false)
      }
    }
  }
}
