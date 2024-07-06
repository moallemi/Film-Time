package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktEpisodeHistory

interface TraktRemoteSource {

  suspend fun seasons(
    traktId: Int,
  ): Result<Map<Int, List<TraktEpisodeHistory>>, GeneralError>

  suspend fun ratings(
    type: String,
    traktId: Int,
  ): Result<Ratings, GeneralError>
}
