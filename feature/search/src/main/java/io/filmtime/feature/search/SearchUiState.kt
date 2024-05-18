package io.filmtime.feature.search

import io.filmtime.data.model.VideoThumbnail

data class SearchUiState(
  val loading: Boolean = false,
  val data: List<VideoThumbnail> = emptyList(),
  val hasResult: Boolean? = null,
)
