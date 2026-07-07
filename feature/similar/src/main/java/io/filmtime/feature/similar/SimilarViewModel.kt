package io.filmtime.feature.similar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType
import io.filmtime.domain.tmdb.movies.GetSimilarMoviesUseCase
import io.filmtime.domain.tmdb.shows.GetSimilarShowsUseCase
import io.filmtime.feature.similar.SimilarAction.LoadSimilar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class SimilarViewModel @Inject constructor(
  private val getSimilarMovies: GetSimilarMoviesUseCase,
  private val getSimilarShows: GetSimilarShowsUseCase,
) : ViewModel() {

  private val pendingActions = MutableSharedFlow<SimilarAction>()

  private val _similarState = MutableStateFlow(SimilarUiState())
  val state = _similarState.asStateFlow()

  init {
    collectActions()
  }

  fun submitAction(action: SimilarAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is LoadSimilar -> loadSimilar(action.videoId, action.videoType)
      }
    }
  }

  private fun loadSimilar(
    videoId: Int,
    videoType: VideoType,
  ) = launch {
    _similarState.value = _similarState.value.copy(isLoading = true, error = null)

    val result = if (videoType == VideoType.Movie) {
      getSimilarMovies(videoId)
    } else {
      getSimilarShows(videoId)
    }

    when (result) {
      is Success -> _similarState.update { state ->
        state.copy(videoItems = result.data, isLoading = false, error = null)
      }

      is Failure -> _similarState.update { state ->
        state.copy(error = result.error.toUiMessage(), isLoading = false)
      }
    }
  }
}
