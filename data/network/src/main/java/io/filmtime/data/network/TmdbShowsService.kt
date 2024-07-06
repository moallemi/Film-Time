package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import io.filmtime.data.network.model.TmdbSeasonResponse
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

  @GET("tv/{series_id}/credits")
  suspend fun getCredit(
    @Path("series_id") seriesId: Int,
  ): NetworkResponse<TmdbCreditsResponse, TmdbErrorResponse>

  @GET("tv/{series_id}/similar")
  suspend fun getSimilar(
    @Path("series_id") seriesId: Int,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>

  @GET("tv/{series_id}/season/{season_number}")
  suspend fun episodesBySeason(
    @Path("series_id") seriesId: Int,
    @Path("season_number") seasonNumber: Int,
  ): NetworkResponse<TmdbSeasonResponse, TmdbErrorResponse>
}
