package io.filmtime.feature.movie.detail

import io.filmtime.data.model.VideoThumbnail

data class MovieDetailState(
  val isLoading: Boolean = false,
  val videoThumbnail: VideoThumbnail? = null,
)
