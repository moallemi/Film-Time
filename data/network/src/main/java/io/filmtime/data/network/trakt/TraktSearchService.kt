package io.filmtime.data.network.trakt

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TraktSearchService {

  @GET("search/{id_type}/{id}?type=movie")
  suspend fun movieIDLookup(
    @Path("id_type") idType: String,
    @Path("id") id: String,
  ): NetworkResponse<List<TraktMovieIDLookupResponse>, TraktErrorResponse>
}
