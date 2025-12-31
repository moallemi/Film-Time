package io.filmtime.data.network.trakt

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TraktAuthService {

  @POST("oauth/token")
  suspend fun getAccessToken(
    @Body body: TraktGetTokenRequest,
  ): NetworkResponse<TraktAccessTokenResponse, TraktErrorResponse>

  @POST("oauth/device/code")
  suspend fun getDeviceCode(
    @Body body: TraktClientIDRequest,
  ): NetworkResponse<TraktDeviceCodeResponse, TraktErrorResponse>

  @POST("oauth/device/token")
  suspend fun pollAccessToken(
    @Body body: PollAccessTokenRequest,
  ): NetworkResponse<TraktAccessTokenResponse, TraktErrorResponse>
}
