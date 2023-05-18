package io.filmtime.feature.movie.detail

import io.filmtime.data.model.VideoDetail

data class MovieDetailState(
  val isLoading: Boolean = false,
  val videoDetail: VideoDetail? = null,
)
