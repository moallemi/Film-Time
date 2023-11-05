package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens
import io.filmtime.data.network.BuildConfig
import io.filmtime.data.network.adapter.NetworkResponse
import io.filmtime.data.network.trakt.TraktAuthService
import io.filmtime.data.network.trakt.TraktGetTokenRequest
import javax.inject.Inject

class TraktAuthRemoteSourceImpl @Inject constructor(
  private val traktAuthService: TraktAuthService,
) : TraktAuthRemoteSource {


  override suspend fun getAccessToken(code: String): Result<TraktTokens, GeneralError> {
    val result = traktAuthService.getAccessToken(
      body = TraktGetTokenRequest(
        code = code,
        clientID = BuildConfig.TRAKT_CLIENT_ID,
        clientSecret = BuildConfig.TRAKT_CLIENT_SECRET,
        grantType = "authorization_code",
        redirectURI = "filmtime://",
      ),
    )
    return when (result) {
      is NetworkResponse.ApiError -> Result.Failure(GeneralError.ApiError(result.body.error, result.code))
      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.Success -> {
        val response = result.body
        if (response == null) {
          Result.Failure(GeneralError.UnknownError(Throwable("Access token response is null")))
        } else {
          Result.Success(response.toAccessToken())
        }
      }
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }
  }
}
