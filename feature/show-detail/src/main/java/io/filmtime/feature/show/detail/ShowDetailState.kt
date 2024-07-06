package io.filmtime.feature.show.detail

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.VideoDetail

data class ShowDetailState(
  val isLoading: Boolean = false,
  val isBookmarked: Boolean = false,
  val videoDetail: VideoDetail? = null,
  val seasons: Map<Int, List<EpisodeThumbnail>> = emptyMap(),
  val ratings: Ratings? = null,
  val message: String? = null,
  val error: UiMessage? = null,
)
