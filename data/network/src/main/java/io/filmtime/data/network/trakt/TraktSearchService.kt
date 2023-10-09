package io.filmtime.data.network.trakt

import io.filmtime.data.network.BuildConfig
import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TraktSearchService {

  @GET("search/{id_type}/{id}?type=movie")
  @Headers(
    "trakt-api-key: ${BuildConfig.TRAKT_CLIENT_ID}",
    "trakt-api-version: 2",
  )
  suspend fun movieIDLookup(
    @Path("id_type") idType: String,
    @Path("id") id: String,
  ): NetworkResponse<List<TraktMovieIDLookupResponse>, TraktErrorResponse>
}
