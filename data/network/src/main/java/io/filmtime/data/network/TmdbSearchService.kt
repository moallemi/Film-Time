package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbSearchService {

  @GET("search/tv")
  suspend fun searchTvShows(
    @Query("query") query: String,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>

  @GET("search/movie")
  suspend fun searchMovies(
    @Query("query") query: String,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("search/multi")
  suspend fun searchMulti(
    @Query("query") query: String,
  ): NetworkResponse<TmdbSearchListResponse, TmdbErrorResponse>
}
