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
    @Query("page") page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/popular")
  suspend fun popular(
    @Query("page") page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/top_rated")
  suspend fun topRated(
    @Query("page") page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/now_playing")
  suspend fun nowPlaying(
    @Query("page") page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/upcoming")
  suspend fun upcoming(
    @Query("page") page: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("movie/{movie_id}/credits")
  suspend fun credits(
    @Path("movie_id") movieId: Int,
  ): NetworkResponse<TmdbCreditsResponse, TmdbErrorResponse>

  @GET("movie/{movie_id}/similar")
  suspend fun getSimilar(
    @Path("movie_id") movieId: Int,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("search/movie")
  suspend fun searchMovies(
    @Query("query") query: String,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>
}
