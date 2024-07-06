package io.filmtime.feature.show.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType.Show
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.tmdb.shows.GetEpisodesBySeasonUseCase
import io.filmtime.domain.tmdb.shows.GetShowCreditsUseCase
import io.filmtime.domain.tmdb.shows.GetShowDetailsUseCase
import io.filmtime.domain.tmdb.shows.GetSimilarShowsUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
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
  private val getShowCreditsUseCase: GetShowCreditsUseCase,
  private val getSimilarShowsUseCase: GetSimilarShowsUseCase,
  private val addBookmark: AddBookmarkUseCase,
  private val deleteBookmark: DeleteBookmarkUseCase,
  private val observeBookmark: ObserveBookmarkUseCase,
  private val getRatings: GetRatingsUseCase,
  private val getEpisodesBySeason: GetEpisodesBySeasonUseCase,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")
  private val _state: MutableStateFlow<ShowDetailState> = MutableStateFlow(ShowDetailState())
  val state = _state.asStateFlow()
  private val _similarState: MutableStateFlow<ShowDetailSimilarState> = MutableStateFlow(ShowDetailSimilarState())
  val similarState = _similarState.asStateFlow()

  private val _creditState: MutableStateFlow<ShowDetailCreditState> = MutableStateFlow(ShowDetailCreditState())
  val creditState = _creditState.asStateFlow()

  init {
    observeBookmark()
    load()
    loadSimilar()
    loadCredits()
  }

  private fun loadSimilar() = viewModelScope.launch {
    _similarState.value = _similarState.value.copy(isLoading = true)
    when (val result = getSimilarShowsUseCase(videoId)) {
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
    when (val result = getShowCreditsUseCase(videoId)) {
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
    _state.value = _state.value.copy(isLoading = true, error = null)

    getShowDetails(videoId)
      .fold(
        onSuccess = { data ->
          _state.update { state -> state.copy(videoDetail = data, isLoading = false) }
          loadRatings()
          loadEpisodesBySeason(seasonNumber = 1)
        },
        onFailure = { e -> _state.update { state -> state.copy(isLoading = false, error = e.toUiMessage()) } },
      )
  }

  private fun loadRatings() = viewModelScope.launch {
    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      getRatings(type = Show, tmdbId = tmdbId)
        .fold(
          onSuccess = { ratings -> _state.update { state -> state.copy(ratings = ratings) } },
          onFailure = { error -> _state.update { state -> state.copy(error = error.toUiMessage()) } },
        )
    }
  }

  private fun loadEpisodesBySeason(seasonNumber: Int) = viewModelScope.launch {
    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      getEpisodesBySeason(tmdbId, seasonNumber)
        .fold(
          onSuccess = { episodes ->
            _state.update { state -> state.copy(seasons = state.seasons + (seasonNumber to episodes)) }
          },
          onFailure = { error -> _state.update { state -> state.copy(error = error.toUiMessage()) } },
        )
    }
  }

  private fun observeBookmark() = viewModelScope.launch {
    observeBookmark(videoId, Show)
      .onEach { isBookmarked ->
        _state.update { state ->
          state.copy(isBookmarked = isBookmarked)
        }
      }
      .collect()
  }

  fun addBookmark() = viewModelScope.launch {
    addBookmark(videoId, Show)
  }

  fun removeBookmark() = viewModelScope.launch {
    deleteBookmark(videoId, Show)
  }
}
