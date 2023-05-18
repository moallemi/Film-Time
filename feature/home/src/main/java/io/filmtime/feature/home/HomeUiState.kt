package io.filmtime.feature.home

import io.filmtime.data.model.VideoThumbnail

data class HomeUiState(
  val isLoading: Boolean,
  val items: List<VideoThumbnail> = emptyList(),
)
