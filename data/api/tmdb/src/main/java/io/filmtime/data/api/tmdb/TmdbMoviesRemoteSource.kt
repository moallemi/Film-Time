package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbMoviesRemoteSource {

  suspend fun getMovieDetails(movieId: Int): Result<VideoDetail, GeneralError>

  suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getPopularMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getTopRatedMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getNowPlayingMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  companion object {
    const val PAGE_SIZE = 20 // TMDB API default page size
  }
}
