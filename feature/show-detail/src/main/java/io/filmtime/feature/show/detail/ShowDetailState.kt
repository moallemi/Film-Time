package io.filmtime.feature.show.detail

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.EpisodeThumbnail
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
)

internal data class SeasonsState(
  val isLoading: Boolean = false,
  val seasons: Map<Int, List<EpisodeThumbnail>> = emptyMap(),
  val error: UiMessage? = null,
)
