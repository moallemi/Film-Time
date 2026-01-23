package io.filmtime.feature.movie.detail

import android.content.Intent
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.MovieVideo
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoDetail

data class MovieDetailState(
  val isLoading: Boolean = false,
  val isBookmarked: Boolean = false,
  val isCollectionLoading: Boolean = false,
  val collection: MovieCollection? = null,
  val videoDetail: VideoDetail? = null,
  val ratings: Ratings? = null,
  val isStreamLoading: Boolean = false,
  val streamInfo: StreamInfo? = null,
  val error: UiMessage? = null,
  val videos: List<MovieVideo>? = null,
  val isTrailersLoading: Boolean = false,
  val installedPlugins: List<PluginMetadata> = emptyList(),
  val showPluginSelection: Boolean = false,
  val showNoPluginsDialog: Boolean = false,
  val streamError: String? = null,
  val pendingAuthPlugin: PluginMetadata? = null,
  val loginIntent: Intent? = null,
)
