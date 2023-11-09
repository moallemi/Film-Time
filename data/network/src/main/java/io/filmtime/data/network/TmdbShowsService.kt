package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbShowsService {

  @GET("tv/{series_id})")
  suspend fun getShowDetails(
    @Path("series_id") seriesId: Int
  ): NetworkResponse<TmdbShowDetailsResponse, TmdbErrorResponse>

  @GET("trending/tv/{time_window}")
  suspend fun getTrendingShows(
    @Path("time_window") timeWindow: String
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>
}
