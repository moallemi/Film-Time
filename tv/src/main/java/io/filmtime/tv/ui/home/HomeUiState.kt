package io.filmtime.tv.ui.home

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.VideoThumbnail

data class HomeUiState(
  val isLoading: Boolean = false,
  val videoSections: List<VideoSection> = emptyList(),
  val error: UiMessage? = null,
)

data class VideoSection(
  val title: String,
  val items: List<VideoThumbnail> = emptyList(),
  val type: SectionType,
)

enum class SectionType {
  TrendingMovies,
  TrendingShows,
}
