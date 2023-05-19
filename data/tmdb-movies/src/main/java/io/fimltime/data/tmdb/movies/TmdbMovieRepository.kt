package io.fimltime.data.tmdb.movies

import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbMovieRepository {

  suspend fun getMovieDetails(movieId: Int): VideoDetail

  suspend fun getTrendingMovies(): List<VideoThumbnail>

  suspend fun getPopularMovies(): List<VideoThumbnail>

  suspend fun getTopRatedMovies(): List<VideoThumbnail>

  suspend fun getNowPlayingMovies(): List<VideoThumbnail>
}
