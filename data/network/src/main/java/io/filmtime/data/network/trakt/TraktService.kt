package io.filmtime.data.network.trakt

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TraktService {

  /**
   * [idType] can be one of [movies, shows]
   * [id] is the trakt id of the movie or show
   */
  @GET("{type}/{id}/ratings?extended=all")
  suspend fun ratings(
    @Path("type") idType: String,
    @Path("id") id: Int,
  ): NetworkResponse<TraktExtendedRatingsResponse, TraktErrorResponse>

  @GET("shows/{id}/seasons?extended=episodes")
  suspend fun seasons(
    @Path("id") traktId: Int,
  ): NetworkResponse<List<TraktExtendedSeasonResponse>, TraktErrorResponse>
}
