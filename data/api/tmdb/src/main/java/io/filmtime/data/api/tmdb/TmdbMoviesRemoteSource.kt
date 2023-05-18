package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoDetail

interface TmdbMoviesRemoteSource {

  suspend fun getMovieDetails(movieId: Int): VideoDetail
}
