package io.filmtime.feature.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.plugin.api.StreamRequest
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.SubtitleInfo
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.plugin.CreatePluginLoginIntentUseCase
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
import io.filmtime.domain.plugin.GetStreamFromPluginUseCase
import io.filmtime.domain.plugin.RefreshPluginsUseCase
import io.filmtime.domain.tmdb.movies.GetMovieCollectionUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.tmdb.movies.GetMovieVideosUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
import io.filmtime.feature.movie.detail.MovieDetailAction.AddBookmark
import io.filmtime.feature.movie.detail.MovieDetailAction.DismissNoPluginsDialog
import io.filmtime.feature.movie.detail.MovieDetailAction.DismissPluginSelection
import io.filmtime.feature.movie.detail.MovieDetailAction.Play
import io.filmtime.feature.movie.detail.MovieDetailAction.PluginLoginResult
import io.filmtime.feature.movie.detail.MovieDetailAction.Reload
import io.filmtime.feature.movie.detail.MovieDetailAction.RemoveBookmark
import io.filmtime.feature.movie.detail.MovieDetailAction.SelectPlugin
import io.filmtime.feature.movie.detail.MovieDetailNavigationEvent.NavigateToPlayer
import io.filmtime.feature.plugin.manager.PluginPreferences
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
internal class MovieDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMovieDetail: GetMovieDetailsUseCase,
  private val addBookmark: AddBookmarkUseCase,
  private val deleteBookmark: DeleteBookmarkUseCase,
  private val observeBookmark: ObserveBookmarkUseCase,
  private val getRatings: GetRatingsUseCase,
  private val getCollection: GetMovieCollectionUseCase,
  private val getMovieVideos: GetMovieVideosUseCase,
  private val getInstalledPlugins: GetInstalledPluginsUseCase,
  private val refreshPlugins: RefreshPluginsUseCase,
  private val getStreamFromPlugin: GetStreamFromPluginUseCase,
  private val createPluginLoginIntent: CreatePluginLoginIntentUseCase,
  private val pluginPreferences: PluginPreferences,
) : ViewModel() {

  private val videoId: Int = savedStateHandle["video_id"] ?: throw IllegalStateException("videoId is required")

  private val pendingActions = MutableSharedFlow<MovieDetailAction>()

  private val _state = MutableStateFlow(MovieDetailState())
  val state = _state.asStateFlow()

  private val _navigationEvents = MutableSharedFlow<MovieDetailNavigationEvent>()
  val navigationEvents = _navigationEvents.asSharedFlow()

  init {
    collectActions()
    loadMovieDetail()
    observeBookmark()
    loadVideos()
    observePlugins()
    refreshPluginList()
  }

  fun submitAction(action: MovieDetailAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is Reload -> reload()
        is Play -> loadStreamInfo()
        is AddBookmark -> addBookmark()
        is RemoveBookmark -> removeBookmark()
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

  private fun reload() {
    loadMovieDetail()
    loadVideos()
  }

  private fun loadMovieDetail() = launch {
    _state.value = _state.value.copy(isLoading = true, error = null)

    getMovieDetail(videoId)
      .collect { result ->
        result.fold(
          onSuccess = { data ->
            _state.update { state ->
              state.copy(
                videoDetail = data,
                isLoading = false,
              )
            }
            loadRatings()
            loadCollection(data.collectionId?.toInt())
          },
          onFailure = { e -> _state.update { state -> state.copy(isLoading = false, error = e.toUiMessage()) } },
        )
      }
  }

  private fun loadRatings() = launch {
    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      getRatings(type = Movie, tmdbId = tmdbId)
        .fold(
          onSuccess = { ratings -> _state.update { state -> state.copy(ratings = ratings) } },
          onFailure = { error -> _state.update { state -> state.copy(error = error.toUiMessage()) } },
        )
    }
  }

  private fun loadStreamInfo() {
    val plugins = _state.value.installedPlugins
    when {
      plugins.isEmpty() -> {
        _state.update { it.copy(showNoPluginsDialog = true) }
      }
      plugins.size == 1 -> {
        loadStreamFromPlugin(plugins.first())
      }
      else -> {
        val defaultPluginId = pluginPreferences.getDefaultPluginId()
        val defaultPlugin = plugins.find { it.pluginId == defaultPluginId }
        if (defaultPlugin != null) {
          loadStreamFromPlugin(defaultPlugin)
        } else {
          _state.update { it.copy(showPluginSelection = true) }
        }
      }
    }
  }

  private fun onPluginSelected(plugin: PluginMetadata) {
    _state.update { it.copy(showPluginSelection = false) }
    loadStreamFromPlugin(plugin)
  }

  private fun dismissPluginSelection() {
    _state.update { it.copy(showPluginSelection = false) }
  }

  private fun dismissNoPluginsDialog() {
    _state.update { it.copy(showNoPluginsDialog = false) }
  }

  private fun loadStreamFromPlugin(plugin: PluginMetadata) = launch {
    val videoDetail = _state.value.videoDetail ?: return@launch
    val tmdbId = videoDetail.ids.tmdbId ?: return@launch

    _state.update { it.copy(isStreamLoading = true, streamError = null) }

    val request = StreamRequest.Movie(
      tmdbId = tmdbId,
      imdbId = null,
      title = videoDetail.title,
      year = videoDetail.year,
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
          _state.update { it.copy(streamInfo = streamInfo, isStreamLoading = false) }
          _navigationEvents.emit(NavigateToPlayer(streamInfo))
        } else {
          _state.update {
            it.copy(
              isStreamLoading = false,
              streamError = "No streams available from ${plugin.name}",
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
            )
          }
        }
      },
    )
  }

  private fun onPluginLoginResult(success: Boolean) {
    val plugin = _state.value.pendingAuthPlugin
    _state.update { it.copy(pendingAuthPlugin = null, loginIntent = null) }
    if (success && plugin != null) {
      loadStreamFromPlugin(plugin)
    }
  }

  private fun loadCollection(collectionId: Int?) = launch {
    if (collectionId == null) return@launch
    _state.value = _state.value.copy(isCollectionLoading = true)
    getCollection(collectionId)
      .fold(
        onSuccess = { collections ->
          _state.update {
            it.copy(
              isCollectionLoading = false,
              collection = collections,
            )
          }
        },
        onFailure = { error ->
          _state.update { state -> state.copy(error = error.toUiMessage()) }
        },
      )
  }

  private fun observeBookmark() = launch {
    observeBookmark(videoId, Movie)
      .onEach { isBookmarked ->
        _state.update { state ->
          state.copy(isBookmarked = isBookmarked)
        }
      }
      .collect()
  }

  private fun addBookmark() = launch {
    addBookmark(videoId, Movie)
  }

  private fun removeBookmark() = launch {
    deleteBookmark(videoId, Movie)
  }

  private fun loadVideos() = launch {
    _state.update { state -> state.copy(isTrailersLoading = true) }
    getMovieVideos(videoId)
      .fold(
        onSuccess = {
          _state.update { state ->
            state.copy(
              videos = it,
              isTrailersLoading = false,
            )
          }
        },
        onFailure = {
          _state.update { state -> state.copy(isTrailersLoading = false) }
        },
      )
  }
}
