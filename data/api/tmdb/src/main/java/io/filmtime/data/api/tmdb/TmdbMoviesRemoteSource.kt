package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbMoviesRemoteSource {

  suspend fun getMovieDetails(movieId: Int): Result<VideoDetail, GeneralError>

  suspend fun getTrendingMovies(page:Long): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getPopularMovies(page:Long): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getTopRatedMovies(page:Long): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getNowPlayingMovies(page:Long): Result<List<VideoThumbnail>, GeneralError>
}
