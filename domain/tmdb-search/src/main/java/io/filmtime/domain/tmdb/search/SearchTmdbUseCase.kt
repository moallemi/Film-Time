package io.filmtime.domain.tmdb.search

import androidx.paging.PagingData
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import kotlinx.coroutines.flow.Flow

interface SearchTmdbUseCase {
  operator fun invoke(query: String, type: SearchType): Flow<PagingData<SearchResult>>
}
