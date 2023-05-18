package io.fimltime.data.tmdb.movies

import io.filmtime.data.model.VideoDetail

interface TmdbMovieRepository {

  suspend fun getMovieDetails(movieId: Int): VideoDetail
}
