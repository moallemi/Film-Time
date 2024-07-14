package io.filmtime.data.tmdb.search

import androidx.paging.PagingData
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import kotlinx.coroutines.flow.Flow

interface TmdbSearchRepository {

  fun streamSearch(searchType: SearchType, query: String): Flow<PagingData<SearchResult>>
}
