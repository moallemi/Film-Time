package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbDiscoverService {

  @GET("discover/movie")
  suspend fun getMovies(
    @Query("page") page: Int?,
    @Query("with_genres") genres: List<String>?,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>

  @GET("discover/tv")
  suspend fun getShows(
    @Query("page") page: Int?,
    @Query("with_genres") genres: List<String>?,
  ): NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>
}
