package io.filmtime.data.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoType

interface TraktRepository {

  suspend fun ratings(
    type: VideoType,
    tmdbId: Int,
  ): Result<Ratings, GeneralError>
}
