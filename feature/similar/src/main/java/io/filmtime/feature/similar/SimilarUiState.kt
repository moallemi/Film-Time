package io.filmtime.feature.similar

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.VideoThumbnail

data class SimilarUiState(
  val isLoading: Boolean = false,
  val videoItems: List<VideoThumbnail> = emptyList(),
  val error: UiMessage? = null,
)
