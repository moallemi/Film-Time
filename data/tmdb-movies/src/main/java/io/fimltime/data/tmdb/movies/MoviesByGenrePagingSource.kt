package io.fimltime.data.tmdb.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.toThrowable
import javax.inject.Inject

class MoviesByGenrePagingSource @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
  private val genreId: Long,
) : PagingSource<Int, VideoThumbnail>() {

  override fun getRefreshKey(state: PagingState<Int, VideoThumbnail>): Int =
    STARTING_PAGE_INDEX

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoThumbnail> = try {
    val page = params.key ?: STARTING_PAGE_INDEX
    when (val response = tmdbMoviesRemoteSource.getByGenres(page = page, listOf(genreId))) {
      is Success -> {
        val movies = response.data
        LoadResult.Page(
          data = movies,
          prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
          nextKey = if (movies.size < PAGE_SIZE) null else page + 1,
        )
      }

      is Failure -> LoadResult.Error(response.error.toThrowable())
    }
  } catch (e: Exception) {
    LoadResult.Error(e)
  }

  companion object {
    private const val STARTING_PAGE_INDEX = 1
    private const val PAGE_SIZE = TmdbMoviesRemoteSource.PAGE_SIZE
  }
}
