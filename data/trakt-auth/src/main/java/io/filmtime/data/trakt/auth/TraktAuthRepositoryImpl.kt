package io.filmtime.data.trakt.auth

import io.filmtime.data.api.trakt.TraktAuthRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens
import io.filmtime.data.storage.trakt.TraktAuthLocalSource
import javax.inject.Inject

class TraktAuthRepositoryImpl @Inject constructor(
  private val traktAuthRemoteSource: TraktAuthRemoteSource,
  private val traktAuthLocalSource: TraktAuthLocalSource,
) : TraktAuthRepository {
  override suspend fun getAccessTokenByCode(code: String): Result<TraktTokens, GeneralError> {
    val result = traktAuthRemoteSource.getAccessToken(code)
    if (result is Result.Success) {
      traktAuthLocalSource.storeAuthTokens(result.data)
    }
    return result
  }

  override suspend fun refreshTokenByAccessToken(accessToken: String) {
    TODO("Not yet implemented")
  }
}
