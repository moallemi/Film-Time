package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

enum class TraktMediaType(val queryName: String) {
  Movie("movie"),
  Show("show"),
}

interface TraktSearchRemoteSource {

  suspend fun getByTmdbId(id: Int, type: TraktMediaType): Result<Int, GeneralError>
}
