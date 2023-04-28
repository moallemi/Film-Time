package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoThumbnail

interface TmdbMoviesRemoteSource {

  suspend fun getMovieDetails(movieId: Int): VideoThumbnail
}
