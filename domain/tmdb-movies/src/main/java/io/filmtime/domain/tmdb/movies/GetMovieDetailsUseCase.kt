package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import kotlinx.coroutines.flow.Flow

interface GetMovieDetailsUseCase {
  suspend operator fun invoke(movieId: Int): Flow<Result<VideoDetail, GeneralError>>
}
