package io.filmtime.feature.show.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.plugin.api.StreamRequest
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.SubtitleInfo
import io.filmtime.data.model.VideoType.Show
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.plugin.CreatePluginLoginIntentUseCase
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
import io.filmtime.domain.plugin.GetStreamFromPluginUseCase
import io.filmtime.domain.plugin.RefreshPluginsUseCase
import io.filmtime.domain.tmdb.shows.GetEpisodesBySeasonUseCase
import io.filmtime.domain.tmdb.shows.GetShowDetailsUseCase
import io.filmtime.domain.tmdb.shows.GetShowVideosUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
import io.filmtime.domain.trakt.history.AddEpisodeToHistoryUseCase
import io.filmtime.domain.trakt.history.IsShowWatchedUseCase
import io.filmtime.domain.trakt.history.RemoveEpisodeFromHistoryUseCase
import io.filmtime.feature.plugin.manager.PluginPreferences
import io.filmtime.feature.show.detail.ShowDetailAction.AddBookmark
import io.filmtime.feature.show.detail.ShowDetailAction.AddEpisodeToHistory
import io.filmtime.feature.show.detail.ShowDetailAction.ChangeSeason
import io.filmtime.feature.show.detail.ShowDetailAction.DismissNoPluginsDialog
import io.filmtime.feature.show.detail.ShowDetailAction.DismissPluginSelection
import io.filmtime.feature.show.detail.ShowDetailAction.PlayEpisode
import io.filmtime.feature.show.detail.ShowDetailAction.PluginLoginResult
import io.filmtime.feature.show.detail.ShowDetailAction.Reload
import io.filmtime.feature.show.detail.ShowDetailAction.RemoveBookmark
import io.filmtime.feature.show.detail.ShowDetailAction.RemoveEpisodeFromHistory
import io.filmtime.feature.show.detail.ShowDetailAction.SelectPlugin
import io.filmtime.feature.show.detail.ShowDetailNavigationEvent.NavigateToPlayer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
  private val getShowVideos: GetShowVideosUseCase,
  private val getInstalledPlugins: GetInstalledPluginsUseCase,
  private val refreshPlugins: RefreshPluginsUseCase,
  private val getStreamFromPlugin: GetStreamFromPluginUseCase,
  private val createPluginLoginIntent: CreatePluginLoginIntentUseCase,
  private val pluginPreferences: PluginPreferences,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")
  private val _state: MutableStateFlow<ShowDetailState> = MutableStateFlow(ShowDetailState())
  val state = _state.asStateFlow()

  private val pendingActions = MutableSharedFlow<ShowDetailAction>()

  private val _navigationEvents = MutableSharedFlow<ShowDetailNavigationEvent>()
  val navigationEvents = _navigationEvents.asSharedFlow()

  init {
    collectActions()
    observeBookmark()
    load()
    loadVideos()
    observePlugins()
    refreshPluginList()
  }

  fun submitAction(action: ShowDetailAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is Reload -> load()
        is AddBookmark -> addBookmark()
        is RemoveBookmark -> removeBookmark()
        is ChangeSeason -> changeSeason(action.seasonNumber)
        is AddEpisodeToHistory -> addEpisodeToHistory(action.episode)
        is RemoveEpisodeFromHistory -> removeEpisodeFromHistory(action.episode)
        is PlayEpisode -> playEpisode(action.episode)
        is SelectPlugin -> onPluginSelected(action.plugin)
        is DismissPluginSelection -> dismissPluginSelection()
        is DismissNoPluginsDialog -> dismissNoPluginsDialog()
        is PluginLoginResult -> onPluginLoginResult(action.success)
      }
    }
  }

  private fun observePlugins() {
    getInstalledPlugins()
      .onEach { plugins ->
        _state.update { it.copy(installedPlugins = plugins) }
      }
      .launchIn(viewModelScope)
  }

  private fun refreshPluginList() {
    launch {
      refreshPlugins()
    }
  }

  private fun load() = launch {
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

  private fun loadRatings() = launch {
    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      getRatings(type = Show, tmdbId = tmdbId)
        .fold(
          onSuccess = { ratings -> _state.update { state -> state.copy(ratings = ratings) } },
          onFailure = { error -> _state.update { state -> state.copy(error = error.toUiMessage()) } },
        )
    }
  }

  private fun loadEpisodesBySeason(seasonNumber: Int) = launch {
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

  private fun observeBookmark() = launch {
    observeBookmark(videoId, Show)
      .onEach { isBookmarked ->
        _state.update { state ->
          state.copy(isBookmarked = isBookmarked)
        }
      }
      .collect()
  }

  private fun addBookmark() = launch {
    addBookmark(videoId, Show)
  }

  private fun removeBookmark() = launch {
    deleteBookmark(videoId, Show)
  }

  private fun changeSeason(seasonNumber: Int) {
    if (_state.value.seasonsState.seasons[seasonNumber] == null) {
      loadEpisodesBySeason(seasonNumber)
    }
  }

  private fun addEpisodeToHistory(episodeThumbnail: EpisodeThumbnail) = launch {
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

  private fun removeEpisodeFromHistory(episodeThumbnail: EpisodeThumbnail) = launch {
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

  private fun loadVideos() = launch {
    _state.update { state -> state.copy(isTrailersLoading = true) }
    getShowVideos(videoId)
      .fold(
        onSuccess = {
          _state.update { state ->
            state.copy(
              isTrailersLoading = false,
              videos = it,
            )
          }
        },
        onFailure = {
          _state.update { state -> state.copy(isTrailersLoading = false) }
        },
      )
  }

  private fun playEpisode(episode: EpisodeThumbnail) {
    val plugins = _state.value.installedPlugins
    _state.update { it.copy(pendingEpisode = episode) }
    when {
      plugins.isEmpty() -> {
        _state.update { it.copy(showNoPluginsDialog = true) }
      }
      plugins.size == 1 -> {
        loadStreamFromPlugin(plugins.first(), episode)
      }
      else -> {
        val defaultPluginId = pluginPreferences.getDefaultPluginId()
        val defaultPlugin = plugins.find { it.pluginId == defaultPluginId }
        if (defaultPlugin != null) {
          loadStreamFromPlugin(defaultPlugin, episode)
        } else {
          _state.update { it.copy(showPluginSelection = true) }
        }
      }
    }
  }

  private fun onPluginSelected(plugin: PluginMetadata) {
    _state.update { it.copy(showPluginSelection = false) }
    val episode = _state.value.pendingEpisode ?: return
    loadStreamFromPlugin(plugin, episode)
  }

  private fun dismissPluginSelection() {
    _state.update { it.copy(showPluginSelection = false, pendingEpisode = null) }
  }

  private fun dismissNoPluginsDialog() {
    _state.update { it.copy(showNoPluginsDialog = false, pendingEpisode = null) }
  }

  private fun loadStreamFromPlugin(plugin: PluginMetadata, episode: EpisodeThumbnail) = launch {
    val videoDetail = _state.value.videoDetail ?: return@launch
    val tmdbId = videoDetail.ids.tmdbId ?: return@launch

    _state.update { it.copy(isStreamLoading = true, streamError = null) }

    val request = StreamRequest.Show(
      tmdbId = tmdbId,
      imdbId = null,
      title = videoDetail.title,
      year = videoDetail.year,
      season = episode.seasonNumber,
      episode = episode.episodeNumber,
    )

    getStreamFromPlugin(plugin.pluginId, request).fold(
      onSuccess = { response ->
        val firstStream = response.streams.firstOrNull()
        if (firstStream != null) {
          val streamInfo = StreamInfo(
            url = firstStream.url,
            quality = firstStream.quality,
            streamType = firstStream.streamType,
            title = firstStream.title,
            headers = firstStream.headers,
            subtitles = firstStream.subtitles.map { subtitle ->
              SubtitleInfo(
                url = subtitle.url,
                language = subtitle.language,
                label = subtitle.label,
              )
            },
          )
          _state.update { it.copy(isStreamLoading = false, pendingEpisode = null) }
          _navigationEvents.emit(NavigateToPlayer(streamInfo))
        } else {
          _state.update {
            it.copy(
              isStreamLoading = false,
              streamError = "No streams available from ${plugin.name}",
              pendingEpisode = null,
            )
          }
        }
      },
      onFailure = { error ->
        if (error is PluginError.AuthenticationRequired) {
          val intent = createPluginLoginIntent(plugin)
          _state.update {
            it.copy(
              isStreamLoading = false,
              pendingAuthPlugin = plugin,
              loginIntent = intent,
            )
          }
        } else {
          _state.update {
            it.copy(
              isStreamLoading = false,
              streamError = "Failed to get stream from ${plugin.name}",
              pendingEpisode = null,
            )
          }
        }
      },
    )
  }

  private fun onPluginLoginResult(success: Boolean) {
    val plugin = _state.value.pendingAuthPlugin
    val episode = _state.value.pendingEpisode
    _state.update { it.copy(pendingAuthPlugin = null, loginIntent = null) }
    if (success && plugin != null && episode != null) {
      loadStreamFromPlugin(plugin, episode)
    } else {
      _state.update { it.copy(pendingEpisode = null) }
    }
  }
}
