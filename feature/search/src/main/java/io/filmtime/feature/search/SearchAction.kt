package io.filmtime.feature.search

import io.filmtime.data.model.SearchType

internal sealed interface SearchAction {
  data class Search(val query: String, val type: SearchType) : SearchAction
}
