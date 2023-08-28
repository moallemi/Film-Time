package io.filmtime.feature.movie.detail

import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoDetail

data class MovieDetailState(
  val isLoading: Boolean = false,
  val videoDetail: VideoDetail? = null,
  val isStreamLoading: Boolean = false,
  val streamInfo: StreamInfo? = null,
)
