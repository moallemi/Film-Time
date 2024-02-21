package io.filmtime.feature.movies

import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail

internal data class MoviesUiState(
  val isLoading: Boolean,
  val videoSections: List<VideoSection> = emptyList(),
)

internal data class VideoSection(
  val title: String,
  val items: List<VideoThumbnail> = emptyList(),
  val type: VideoListType,
)
