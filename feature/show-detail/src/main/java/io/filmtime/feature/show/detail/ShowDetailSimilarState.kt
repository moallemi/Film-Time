package io.filmtime.feature.show.detail

import io.filmtime.data.model.VideoThumbnail

data class ShowDetailSimilarState(
  val isLoading: Boolean = false,
  val errorMessage: String = "",
  val videoItems: List<VideoThumbnail> = emptyList(),
)
