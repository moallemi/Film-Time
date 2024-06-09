package io.filmtime.feature.movie.detail

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.VideoThumbnail

data class MovieDetailSimilarState(
  val isLoading: Boolean = false,
  val videoItems: List<VideoThumbnail> = emptyList(),
  val error: UiMessage? = null,
)
