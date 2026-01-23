package io.filmtime.feature.show.detail

import android.content.Intent
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.MovieVideo
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.VideoDetail

internal data class ShowDetailState(
  val isLoading: Boolean = false,
  val isBookmarked: Boolean = false,
  val videoDetail: VideoDetail? = null,
  val seasonsState: SeasonsState = SeasonsState(),
  val ratings: Ratings? = null,
  val message: String? = null,
  val error: UiMessage? = null,
  val videos: List<MovieVideo>? = null,
  val isTrailersLoading: Boolean = false,
  val installedPlugins: List<PluginMetadata> = emptyList(),
  val showPluginSelection: Boolean = false,
  val showNoPluginsDialog: Boolean = false,
  val isStreamLoading: Boolean = false,
  val streamError: String? = null,
  val pendingEpisode: EpisodeThumbnail? = null,
  val pendingAuthPlugin: PluginMetadata? = null,
  val loginIntent: Intent? = null,
)

internal data class SeasonsState(
  val isLoading: Boolean = false,
  val seasons: Map<Int, List<EpisodeThumbnail>> = emptyMap(),
  val error: UiMessage? = null,
)
