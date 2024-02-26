package io.filmtime.feature.movie.detail

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoDetail

data class MovieDetailCastState(
  val isLoading: Boolean = false,
  val videoDetail: VideoDetail? = null,
  val message: String? = null,
  val error: GeneralError? = null,
)
