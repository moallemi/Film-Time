package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbShowsService {

  @GET("trending/tv/{time_window}")
  suspend fun getTrendingShows(
    @Path("time_window") timeWindow: String,
    @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>
}
