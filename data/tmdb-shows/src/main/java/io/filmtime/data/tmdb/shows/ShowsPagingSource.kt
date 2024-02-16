package io.filmtime.data.tmdb.shows

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.api.tmdb.TmdbShowsRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.toThrowable
import io.filmtime.data.tmdb.shows.ShowListType.AiringToday
import io.filmtime.data.tmdb.shows.ShowListType.OnTheAir
import io.filmtime.data.tmdb.shows.ShowListType.Popular
import io.filmtime.data.tmdb.shows.ShowListType.TopRated

internal class ShowsPagingSource(
  private val tmdbShowsRemoteSource: TmdbShowsRemoteSource,
  private val showListType: ShowListType,
) : PagingSource<Int, VideoThumbnail>() {

  override fun getRefreshKey(state: PagingState<Int, VideoThumbnail>): Int? =
    STARTING_PAGE_INDEX

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoThumbnail> = try {
    val page = params.key ?: STARTING_PAGE_INDEX
    when (val response = showsList(page = page)) {
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

  private suspend fun showsList(page: Int): Result<List<VideoThumbnail>, GeneralError> =
    when (showListType) {
      Popular -> tmdbShowsRemoteSource.popularShows(page)
      TopRated -> tmdbShowsRemoteSource.topRatedShows(page)
      OnTheAir -> tmdbShowsRemoteSource.onTheAirShows(page)
      AiringToday -> tmdbShowsRemoteSource.airingTodayShows(page)
    }

  companion object {
    private const val STARTING_PAGE_INDEX = 1
    private const val PAGE_SIZE = TmdbMoviesRemoteSource.PAGE_SIZE
  }
}
