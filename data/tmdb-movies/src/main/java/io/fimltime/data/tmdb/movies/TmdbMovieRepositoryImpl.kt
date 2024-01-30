package io.fimltime.data.tmdb.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.api.trakt.TraktSyncRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
  private val traktMovieSearchRemoteSource: TraktSearchRemoteSource,
  private val traktSyncRemoteSource: TraktSyncRemoteSource,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): Result<VideoDetail, GeneralError> {
    return when (val result = tmdbMoviesRemoteSource.getMovieDetails(movieId)) {
      is Result.Failure -> result
      is Result.Success -> {
        return when (val traktIdResult = traktMovieSearchRemoteSource.getByTmdbId(result.data.ids.tmdbId.toString())) {
          is Result.Failure -> traktIdResult
          is Result.Success -> {
            val traktId = traktIdResult.data.toInt()
            return when (val watched = traktSyncRemoteSource.getHistoryById(traktId.toString())) {
              is Result.Failure -> result.run {
                copy(
                  data = data.copy(
                    ids = data.ids.copy(
                      traktId = traktId,
                    ),
                  ),
                )
              }

              is Result.Success -> result.run {
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

  override suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTrendingMovies()

  override suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getPopularMovies(page = 1)

  override suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTopRatedMovies(page = 1)

  override suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getNowPlayingMovies(page = 1)

  override fun getTrendingMoviesStream(): Flow<PagingData<VideoThumbnail>> =
    Pager(
      config = PagingConfig(
        pageSize = TmdbMoviesRemoteSource.PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        MoviesPagingSource(
          tmdbMoviesRemoteSource = tmdbMoviesRemoteSource,
        )
      },
    ).flow
}
