package io.filmtime.feature.home

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.VideoThumbnail

internal data class HomeUiState(
  val isLoading: Boolean,
  val videoSections: List<VideoSection> = emptyList(),
  val error: UiMessage? = null,
)

internal data class VideoSection(
  val title: String,
  val items: List<VideoThumbnail> = emptyList(),
  val type: SectionType,
)

internal enum class SectionType {
  TrendingMovies,
  TrendingShows,
}
