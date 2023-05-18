package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbMoviesRemoteSource {

  suspend fun getMovieDetails(movieId: Int): VideoDetail

  suspend fun getTrendingMovies(): List<VideoThumbnail>
}
