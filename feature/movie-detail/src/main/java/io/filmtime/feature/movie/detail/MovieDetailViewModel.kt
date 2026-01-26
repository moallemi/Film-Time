package io.filmtime.feature.movie.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.plugin.api.StreamRequest
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
import io.filmtime.feature.plugin.manager.PluginPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
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

  private val _state = MutableStateFlow(MovieDetailState())
  val state = _state.asStateFlow()

  val navigateToPlayer = MutableSharedFlow<StreamInfo?>()

  init {
    loadMovieDetail()
    observeBookmark()
    loadVideos()
    observePlugins()
    refreshPluginList()
  }

  private fun observePlugins() {
    getInstalledPlugins()
      .onEach { plugins ->
        _state.update { it.copy(installedPlugins = plugins) }
      }
      .launchIn(viewModelScope)
  }

  private fun refreshPluginList() {
    viewModelScope.launch {
      refreshPlugins()
    }
  }

  fun reload() {
    loadMovieDetail()
    loadVideos()
  }

  private fun loadMovieDetail() = viewModelScope.launch {
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

  private fun loadRatings() = viewModelScope.launch {
    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      getRatings(type = Movie, tmdbId = tmdbId)
        .fold(
          onSuccess = { ratings -> _state.update { state -> state.copy(ratings = ratings) } },
          onFailure = { error -> _state.update { state -> state.copy(error = error.toUiMessage()) } },
        )
    }
  }

  fun loadStreamInfo() {
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

  fun onPluginSelected(plugin: PluginMetadata) {
    _state.update { it.copy(showPluginSelection = false) }
    loadStreamFromPlugin(plugin)
  }

  fun dismissPluginSelection() {
    _state.update { it.copy(showPluginSelection = false) }
  }

  fun dismissNoPluginsDialog() {
    _state.update { it.copy(showNoPluginsDialog = false) }
  }

  private fun loadStreamFromPlugin(plugin: PluginMetadata) = viewModelScope.launch {
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
          navigateToPlayer.emit(streamInfo)
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

  fun onPluginLoginResult(success: Boolean) {
    val plugin = _state.value.pendingAuthPlugin
    _state.update { it.copy(pendingAuthPlugin = null, loginIntent = null) }
    if (success && plugin != null) {
      loadStreamFromPlugin(plugin)
    }
  }

  private fun loadCollection(collectionId: Int?) = viewModelScope.launch {
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

  private fun observeBookmark() = viewModelScope.launch {
    observeBookmark(videoId, Movie)
      .onEach { isBookmarked ->
        _state.update { state ->
          state.copy(isBookmarked = isBookmarked)
        }
      }
      .collect()
  }

  fun addBookmark() = viewModelScope.launch {
    addBookmark(videoId, Movie)
  }

  fun removeBookmark() = viewModelScope.launch {
    deleteBookmark(videoId, Movie)
  }

  private fun loadVideos() = viewModelScope.launch {
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
