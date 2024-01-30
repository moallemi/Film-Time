package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbMoviesService {

  @GET("movie/{movie_id}")
  suspend fun getMovieDetails(
    @Path("movie_id") movieId: Int,
  ): NetworkResponse<TmdbMovieDetailsResponse, TmdbErrorResponse>

  @GET("trending/movie/{time_window}")
  suspend fun trending(
    @Path("time_window") timeWindow: String,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/popular")
  suspend fun popular(
    @Query("page") page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/top_rated")
  suspend fun topRated(
    page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/now_playing")
  suspend fun nowPlaying(
    page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>
}
