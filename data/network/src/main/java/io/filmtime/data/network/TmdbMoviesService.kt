package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TmdbMoviesService {

  @GET("movie/{movie_id}")
  suspend fun getMovieDetails(
    @Path("movie_id") movieId: Int,
  ): NetworkResponse<TmdbMovieDetailsResponse, TmdbErrorResponse>

  @GET("trending/movie/{list_type}")
  suspend fun getMovieList(
    @Path("list_type") listType: String,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>
}
