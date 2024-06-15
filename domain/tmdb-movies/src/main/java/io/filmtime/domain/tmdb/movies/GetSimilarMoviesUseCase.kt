package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail

interface GetSimilarMoviesUseCase {

  suspend operator fun invoke(movieId: Int): Result<List<VideoThumbnail>, GeneralError>
}
