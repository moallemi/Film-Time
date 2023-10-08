package io.fimltime.data.tmdb.movies

import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
  private val traktMovieSearchRemoteSource: TraktSearchRemoteSource,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): Result<VideoDetail, GeneralError> {
    return when (val result = tmdbMoviesRemoteSource.getMovieDetails(movieId)) {
      is Result.Failure -> result
      is Result.Success -> {
        return when (val traktIdResult = traktMovieSearchRemoteSource.getByTmdbId(result.data.ids.tmdbId.toString())) {
          is Result.Failure -> traktIdResult
          is Result.Success -> result.run {
            copy(
              data = data.copy(
                ids = data.ids.copy(
                  traktId = traktIdResult.data.toInt(),
                ),
              ),
            )
          }
        }
      }
    }
  }

  override suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTrendingMovies()

  override suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getPopularMovies()

  override suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTopRatedMovies()

  override suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getNowPlayingMovies()
}
