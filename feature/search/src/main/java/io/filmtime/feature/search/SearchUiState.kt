package io.filmtime.feature.search

import io.filmtime.data.model.SearchResult

data class SearchUiState(
  val loading: Boolean = false,
  val data: List<SearchResult> = emptyList(),
  val hasResult: Boolean? = null,
  val error: String? = null,
)
