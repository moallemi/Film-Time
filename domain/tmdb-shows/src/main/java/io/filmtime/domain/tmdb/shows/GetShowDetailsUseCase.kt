package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail

interface GetShowDetailsUseCase {

  suspend operator fun invoke(showId: Int): Result<VideoDetail, GeneralError>
}
