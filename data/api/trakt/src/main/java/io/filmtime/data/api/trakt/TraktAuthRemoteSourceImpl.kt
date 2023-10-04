package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktAccessToken
import io.filmtime.data.network.BuildConfig
import io.filmtime.data.network.TraktAuthService
import io.filmtime.data.network.TraktGetTokenRequest
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

class TraktAuthRemoteSourceImpl @Inject constructor(
  private val traktAuthService: TraktAuthService,
) : TraktAuthRemoteSource {
  override suspend fun getAccessToken(code: String): Result<TraktAccessToken, GeneralError> {
    val result = traktAuthService.getAccessToken(
      TraktGetTokenRequest(
        code = code,
        clientID = BuildConfig.TRAKT_CLIENT_ID,
        clientSecret = BuildConfig.TRAKT_CLIENT_SECRET,
        grantType = "authorization_code",
        redirectURI = "filmtime://",
      ),
    )
    return when (result) {
      is NetworkResponse.ApiError -> TODO()
      is NetworkResponse.NetworkError -> TODO()
      is NetworkResponse.Success -> {
        val response = result.body
        if (response == null) {
          Result.Failure(GeneralError.UnknownError(Throwable("Access token response is null")))
        } else {
          Result.Success(response.toAccessToken())
        }
      }
      is NetworkResponse.UnknownError -> TODO()
    }
  }
}
