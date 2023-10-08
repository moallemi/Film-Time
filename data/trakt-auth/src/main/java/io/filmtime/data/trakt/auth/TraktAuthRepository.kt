package io.filmtime.data.trakt.auth

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens

interface TraktAuthRepository {

  suspend fun getAccessTokenByCode(code: String): Result<TraktTokens, GeneralError>

  suspend fun refreshTokenByAccessToken(accessToken: String)
}
