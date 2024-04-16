package io.filmtime.feature.movie.detail

import io.filmtime.data.model.VideoThumbnail

data class MovieDetailSimilarState(
  val isLoading: Boolean = false,
  val errorMessage: String = "",
  val videoItems: List<VideoThumbnail> = emptyList(),
)
