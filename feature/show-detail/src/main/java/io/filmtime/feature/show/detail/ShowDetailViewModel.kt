package io.filmtime.feature.show.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoType.Show
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.tmdb.shows.GetEpisodesBySeasonUseCase
import io.filmtime.domain.tmdb.shows.GetShowDetailsUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
import io.filmtime.domain.trakt.history.AddEpisodeToHistoryUseCase
import io.filmtime.domain.trakt.history.IsShowWatchedUseCase
import io.filmtime.domain.trakt.history.RemoveEpisodeFromHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShowDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getShowDetails: GetShowDetailsUseCase,
  private val addBookmark: AddBookmarkUseCase,
  private val deleteBookmark: DeleteBookmarkUseCase,
  private val observeBookmark: ObserveBookmarkUseCase,
  private val getRatings: GetRatingsUseCase,
  private val getEpisodesBySeason: GetEpisodesBySeasonUseCase,
  private val isShowWatched: IsShowWatchedUseCase,
  private val addToHistory: AddEpisodeToHistoryUseCase,
  private val removeFromHistory: RemoveEpisodeFromHistoryUseCase,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")
  private val _state: MutableStateFlow<ShowDetailState> = MutableStateFlow(ShowDetailState())
  val state = _state.asStateFlow()

  init {
    observeBookmark()
    load()
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
    _state.update { state ->
      state.copy(
        seasonsState = state.seasonsState.copy(
          isLoading = true,
          error = null,
        ),
      )
    }

    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      val traktHistory = isShowWatched(tmdbId = videoId, seasonNumber = seasonNumber)

      getEpisodesBySeason(tmdbId, seasonNumber)
        .fold(
          onSuccess = { episodes ->
            val episodesWithHistory = episodes.map { episode ->
              val traktInfo =
                traktHistory.successValue()?.get(seasonNumber)?.find { it.episodeNumber == episode.episodeNumber }
                  ?: return@map episode
              episode.copy(
                isWatched = traktInfo.isWatched,
                ids = episode.ids.copy(traktId = traktInfo.traktId),
              )
            }
            _state.update { state ->
              state.copy(
                seasonsState = state.seasonsState.copy(
                  isLoading = false,
                  seasons = state.seasonsState.seasons + (seasonNumber to episodesWithHistory),
                ),
              )
            }
          },
          onFailure = { error ->
            _state.update { state ->
              state.copy(
                seasonsState = state.seasonsState.copy(
                  isLoading = false,
                  error = error.toUiMessage(),
                ),
              )
            }
          },
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

  fun changeSeason(seasonNumber: Int) {
    if (_state.value.seasonsState.seasons[seasonNumber] == null) {
      loadEpisodesBySeason(seasonNumber)
    }
  }

  fun addEpisodeToHistory(episodeThumbnail: EpisodeThumbnail) = viewModelScope.launch {
    _state.update { state ->
      state.copy(
        seasonsState = state.seasonsState.copy(
          seasons = state.seasonsState.seasons.mapValues { seasons ->
            seasons.value.map { episode ->
              if (episode.episodeNumber == episodeThumbnail.episodeNumber) {
                episode.copy(isLoading = true)
              } else {
                episode
              }
            }
          },
        ),
      )
    }

    when (
      addToHistory(
        tmdbId = videoId,
        seasonNumber = episodeThumbnail.seasonNumber,
        episodeNumber = episodeThumbnail.episodeNumber,
      )
    ) {
      is Success -> _state.update { state ->
        state.copy(
          seasonsState = state.seasonsState.copy(
            seasons = state.seasonsState.seasons.mapValues { seasons ->
              seasons.value.map { episode ->
                if (episode.episodeNumber == episodeThumbnail.episodeNumber) {
                  episode.copy(isLoading = false, isWatched = true)
                } else {
                  episode
                }
              }
            },
          ),
        )
      }

      is Failure -> _state.update { state ->
        state.copy(
          seasonsState = state.seasonsState.copy(
            seasons = state.seasonsState.seasons.mapValues { seasons ->
              seasons.value.map { episode ->
                if (episode.episodeNumber == episodeThumbnail.episodeNumber) {
                  episode.copy(isLoading = false, isWatched = false)
                } else {
                  episode
                }
              }
            },
          ),
        )
      }
    }
  }

  fun removeEpisodeFromHistory(episodeThumbnail: EpisodeThumbnail) = viewModelScope.launch {
    _state.update { state ->
      state.copy(
        seasonsState = state.seasonsState.copy(
          seasons = state.seasonsState.seasons.mapValues { seasons ->
            seasons.value.map { episode ->
              if (episode.episodeNumber == episodeThumbnail.episodeNumber) {
                episode.copy(isLoading = true)
              } else {
                episode
              }
            }
          },
        ),
      )
    }

    when (
      removeFromHistory(
        tmdbId = videoId,
        seasonNumber = episodeThumbnail.seasonNumber,
        episodeNumber = episodeThumbnail.episodeNumber,
      )
    ) {
      is Success -> _state.update { state ->
        state.copy(
          seasonsState = state.seasonsState.copy(
            seasons = state.seasonsState.seasons.mapValues { seasons ->
              seasons.value.map { episode ->
                if (episode.episodeNumber == episodeThumbnail.episodeNumber) {
                  episode.copy(isLoading = false, isWatched = false)
                } else {
                  episode
                }
              }
            },
          ),
        )
      }

      is Failure -> _state.update { state ->
        state.copy(
          seasonsState = state.seasonsState.copy(
            seasons = state.seasonsState.seasons.mapValues { seasons ->
              seasons.value.map { episode ->
                if (episode.episodeNumber == episodeThumbnail.episodeNumber) {
                  episode.copy(isLoading = false, isWatched = true)
                } else {
                  episode
                }
              }
            },
          ),
        )
      }
    }
  }
}
