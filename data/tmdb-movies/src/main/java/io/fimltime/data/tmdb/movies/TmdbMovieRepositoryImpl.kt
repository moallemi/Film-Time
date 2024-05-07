package io.fimltime.data.tmdb.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.api.trakt.TraktSyncRemoteSource
import io.filmtime.data.database.MovieDetailDao
import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
  private val traktMovieSearchRemoteSource: TraktSearchRemoteSource,
  private val traktSyncRemoteSource: TraktSyncRemoteSource,
  private val movieDao: MovieDetailDao,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): Flow<Result<VideoDetail, GeneralError>> {
    GlobalScope.launch {
      delay(3000)
      when (val result = tmdbMoviesRemoteSource.movieDetails(movieId)) {
        is Result.Failure -> result
        is Result.Success -> {
          movieDao.storeMovie(result.data.toEntity())
          when (val traktIdResult = traktMovieSearchRemoteSource.getByTmdbId(result.data.ids.tmdbId.toString())) {
            is Result.Failure -> traktIdResult
            is Result.Success -> {
              val traktId = traktIdResult.data.toInt()
              when (val watched = traktSyncRemoteSource.getHistoryById(traktId.toString())) {
                is Result.Failure -> result.run {
                  copy(
                    data = data.copy(
                      ids = data.ids.copy(
                        traktId = traktId,
                      ),
                    ),
                  )
                }

                is Result.Success -> {
                  result.run {
                    copy(
                      data = data.copy(
                        ids = data.ids.copy(
                          traktId = traktId,
                        ),
                        isWatched = watched.data,
                      ),
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
    return movieDao.getMovieByTmdbId(movieId)
      .catch { Result.Failure(it) }
      .map {
        if (it != null) {
          Result.Success(it.toMovie())
        } else {
          Result.Failure(GeneralError.UnknownError(Throwable("Data not found")))
        }
      }
  }

  override suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.trendingMovies(1)

  override suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.popularMovies(page = 1)

  override suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.topRatedMovies(page = 1)

  override suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.nowPlayingMovies(page = 1)

  override suspend fun upcomingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.upcomingMovies(page = 1)

  override fun moviesStream(movieListType: MovieListType): Flow<PagingData<VideoThumbnail>> =
    Pager(
      config = PagingConfig(
        pageSize = TmdbMoviesRemoteSource.PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        MoviesPagingSource(
          tmdbMoviesRemoteSource = tmdbMoviesRemoteSource,
          movieListType = movieListType,
        )
      },
    ).flow

  override suspend fun getCredit(movieId: Int): Result<List<CreditItem>, GeneralError> =
    tmdbMoviesRemoteSource.getCredit(movieId)

  override suspend fun getSimilar(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getSimilar(movieId)
}
