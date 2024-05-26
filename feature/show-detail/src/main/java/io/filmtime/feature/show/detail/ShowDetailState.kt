package io.filmtime.feature.show.detail

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoDetail

data class ShowDetailState(
  val isLoading: Boolean = false,
  val videoDetail: VideoDetail? = null,
  val message: String? = null,
  val streamInfo: StreamInfo? = null,
  val error: UiMessage? = null,
)
