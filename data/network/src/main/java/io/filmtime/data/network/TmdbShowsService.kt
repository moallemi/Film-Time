package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbShowsService {

  @GET("tv/{series_id})")
  suspend fun showDetails(
    @Path("series_id") seriesId: Int,
  ): NetworkResponse<TmdbShowDetailsResponse, TmdbErrorResponse>

  @GET("trending/tv/{time_window}")
  suspend fun trendingShows(
    @Path("time_window") timeWindow: String,
    @Query("page") page: Int,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>

  @GET("tv/popular")
  suspend fun popular(
    @Query("page") page: Int,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>

  @GET("tv/top_rated")
  suspend fun topRated(
    @Query("page") page: Int,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>

  @GET("tv/on_the_air")
  suspend fun onTheAir(
    @Query("page") page: Int,
    @Query("timezone") timezone: String,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>

  @GET("tv/airing_today")
  suspend fun airingToday(
    @Query("page") page: Int,
    @Query("timezone") timezone: String,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>
}
