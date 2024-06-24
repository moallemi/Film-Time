package io.filmtime.domain.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoType

interface GetRatingsUseCase {

  suspend operator fun invoke(type: VideoType, tmdbId: Int): Result<Ratings, GeneralError>
}
