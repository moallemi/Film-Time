package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface TraktAuthService {

  @POST("oauth/token")
  suspend fun getAccessToken(@Body body: TraktGetTokenRequest): NetworkResponse<TraktAccessTokenResponse, TraktErrorResponse>
}
