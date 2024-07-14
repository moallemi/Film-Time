package io.filmtime.data.tmdb.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.api.tmdb.TmdbSearchRemoteSource
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TmdbSearchRepositoryImpl @Inject constructor(
  private val tmdbDataSource: TmdbSearchRemoteSource,
) : TmdbSearchRepository {

  override fun streamSearch(searchType: SearchType, query: String): Flow<PagingData<SearchResult>> =
    Pager(
      config = PagingConfig(
        pageSize = TmdbMoviesRemoteSource.PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        SearchPagingSource(
          tmdbSearchRemoteSource = tmdbDataSource,
          searchType = searchType,
          query = query,
        )
      },
    ).flow
}
