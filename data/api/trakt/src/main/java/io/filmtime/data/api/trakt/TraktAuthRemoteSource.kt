package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens

interface TraktAuthRemoteSource {

  suspend fun getAccessToken(code: String): Result<TraktTokens, GeneralError>
}
