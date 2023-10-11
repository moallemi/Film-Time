package io.filmtime.feature.home

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.VideoThumbnail

data class HomeUiState(
  val isLoading: Boolean,
  val videoSections: List<VideoSection> = emptyList(),
  val error: GeneralError? = null,

)

data class VideoSection(
  val title: String,
  val items: List<VideoThumbnail> = emptyList(),
)
