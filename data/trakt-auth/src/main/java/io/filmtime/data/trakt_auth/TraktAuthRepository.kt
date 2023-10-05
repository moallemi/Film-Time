package io.filmtime.data.trakt_auth

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.TraktTokens
import io.filmtime.data.model.Result

interface TraktAuthRepository {

  suspend fun getAccessTokenByCode(code: String): Result<TraktTokens, GeneralError>

  suspend fun refreshTokenByAccessToken(accessToken: String)
}
