package io.filmtime.data.tmdb.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.api.tmdb.TmdbSearchRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import io.filmtime.data.model.SearchType.All
import io.filmtime.data.model.SearchType.Movie
import io.filmtime.data.model.SearchType.Show
import io.filmtime.data.model.toThrowable

class SearchPagingSource constructor(
  private val tmdbSearchRemoteSource: TmdbSearchRemoteSource,
  private val searchType: SearchType,
  private val query: String,
) : PagingSource<Int, SearchResult>() {
  override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int = STARTING_PAGE_INDEX

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> = try {
    val page = params.key ?: STARTING_PAGE_INDEX
    when (val response = search(page, query)) {
      is Success -> {
        val result = response.data
        LoadResult.Page(
          data = result,
          prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
          nextKey = if (result.size < PAGE_SIZE) null else page + 1,
        )
      }

      is Failure -> LoadResult.Error(response.error.toThrowable())
    }
  } catch (e: Exception) {
    LoadResult.Error(e)
  }

  private suspend fun search(page: Int, query: String): Result<List<SearchResult>, GeneralError> =
    when (searchType) {
      All -> tmdbSearchRemoteSource.searchAll(page, query)
      Movie -> tmdbSearchRemoteSource.searchMovies(page, query)
      Show -> tmdbSearchRemoteSource.searchTvShows(page, query)
    }

  companion object {
    private const val STARTING_PAGE_INDEX = 1
    private const val PAGE_SIZE = TmdbMoviesRemoteSource.PAGE_SIZE
  }
}
