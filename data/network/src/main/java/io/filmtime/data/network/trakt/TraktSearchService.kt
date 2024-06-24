package io.filmtime.data.network.trakt

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TraktSearchService {

  @GET("search/{id_type}/{id}")
  suspend fun movieIDLookup(
    @Path("id_type") idType: String,
    @Path("id") id: Int,
    @Query("type") type: String,
  ): NetworkResponse<List<TraktMovieIDLookupResponse>, TraktErrorResponse>
}
