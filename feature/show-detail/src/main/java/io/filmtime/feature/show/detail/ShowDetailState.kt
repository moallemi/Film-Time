package io.filmtime.feature.show.detail

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoDetail

data class ShowDetailState(
  val isLoading: Boolean = false,
  val videoDetail: VideoDetail? = null,
  val message: String? = null,
  val error: GeneralError? = null,
  val isStreamLoading: Boolean = false,
  val streamInfo: StreamInfo? = null,
)
