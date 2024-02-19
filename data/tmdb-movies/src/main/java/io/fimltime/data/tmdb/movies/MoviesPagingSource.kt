package io.fimltime.data.tmdb.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.toThrowable

internal class MoviesPagingSource(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
  private val movieListType: MovieListType,
) : PagingSource<Int, VideoThumbnail>() {

  override fun getRefreshKey(state: PagingState<Int, VideoThumbnail>): Int? =
    STARTING_PAGE_INDEX

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoThumbnail> = try {
    val page = params.key ?: STARTING_PAGE_INDEX
    when (val response = fetchMovies(page = page)) {
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

  private suspend fun fetchMovies(page: Int): Result<List<VideoThumbnail>, GeneralError> {
    return when (movieListType) {
      MovieListType.Trending -> tmdbMoviesRemoteSource.trendingMovies(page)
      MovieListType.Popular -> tmdbMoviesRemoteSource.popularMovies(page)
      MovieListType.TopRated -> tmdbMoviesRemoteSource.topRatedMovies(page)
      MovieListType.NowPlaying -> tmdbMoviesRemoteSource.nowPlayingMovies(page)
      MovieListType.Upcoming -> tmdbMoviesRemoteSource.upcomingMovies(page)
    }
  }

  companion object {
    private const val STARTING_PAGE_INDEX = 1
    private const val PAGE_SIZE = TmdbMoviesRemoteSource.PAGE_SIZE
  }
}
