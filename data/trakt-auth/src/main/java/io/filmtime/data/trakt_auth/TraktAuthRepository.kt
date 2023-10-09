package io.filmtime.data.trakt_auth

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.TraktTokens
import io.filmtime.data.model.Result
import kotlinx.coroutines.flow.Flow

interface TraktAuthRepository {

  val isLoggedIn: Flow<Boolean>

  suspend fun getAccessTokenByCode(code: String): Result<TraktTokens, GeneralError>

  suspend fun refreshTokenByAccessToken(accessToken: String)
}
