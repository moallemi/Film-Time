package io.fimltime.data.tmdb.movies

import io.filmtime.data.model.VideoThumbnail

interface TmdbMovieRepository {

  suspend fun getMovieDetails(movieId: Int): VideoThumbnail
}
