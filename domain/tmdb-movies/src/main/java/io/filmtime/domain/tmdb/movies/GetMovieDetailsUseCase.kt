package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.VideoThumbnail

interface GetMovieDetailsUseCase {
  suspend operator fun invoke(movieId: Int): VideoThumbnail
}
