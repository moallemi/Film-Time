package io.filmtime.feature.credits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType
import io.filmtime.domain.tmdb.movies.GetMovieCreditsUseCase
import io.filmtime.domain.tmdb.shows.GetShowCreditsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CreditsViewModel @Inject constructor(
  private val getMovieCredits: GetMovieCreditsUseCase,
  private val getShowCredits: GetShowCreditsUseCase,
) : ViewModel() {

  private val _creditState = MutableStateFlow(CreditsUiState())
  val state = _creditState.asStateFlow()

  fun loadCredits(
    videoId: Int,
    videoType: VideoType,
  ) = viewModelScope.launch {
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
