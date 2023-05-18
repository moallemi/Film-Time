package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.VideoDetail

interface GetMovieDetailsUseCase {
  suspend operator fun invoke(movieId: Int): VideoDetail
}
