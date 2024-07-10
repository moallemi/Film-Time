package io.filmtime.data.network.trakt

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TraktPersonService {

  @GET("person/{person_id}")
  suspend fun getPerson(
    @Path("person_id") id: String,
  ): NetworkResponse<TraktPersonResponse, TraktErrorResponse>
}
