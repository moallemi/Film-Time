package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail

interface GetMovieDetailsUseCase {
  suspend operator fun invoke(movieId: Int): Result<VideoDetail, GeneralError>
}
