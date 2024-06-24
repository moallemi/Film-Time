package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result

interface TraktRemoteSource {

  suspend fun ratings(
    type: String,
    traktId: Int,
  ): Result<Ratings, GeneralError>
}
