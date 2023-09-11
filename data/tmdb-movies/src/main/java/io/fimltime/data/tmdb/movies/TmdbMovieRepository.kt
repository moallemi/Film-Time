package io.fimltime.data.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbMovieRepository {

  suspend fun getMovieDetails(movieId: Int): VideoDetail

  suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError>
}
