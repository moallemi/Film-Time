package io.filmtime.feature.home

import io.filmtime.data.model.VideoThumbnail

data class HomeUiState(
  val isLoading: Boolean,
  val videoSections: List<VideoSection> = emptyList(),
)

data class VideoSection(
  val title: String,
  val items: List<VideoThumbnail> = emptyList(),
)
