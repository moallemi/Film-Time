package io.filmtime.feature.shows

import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail

internal data class ShowsUiState(
  val isLoading: Boolean,
  val videoSections: List<VideoSection> = emptyList(),
)

internal data class VideoSection(
  val title: String,
  val items: List<VideoThumbnail> = emptyList(),
  val type: VideoListType,
)

internal enum class SectionType {
  TrendingMovies,
  TrendingShows,
}
