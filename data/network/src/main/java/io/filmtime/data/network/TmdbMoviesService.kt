package io.filmtime.data.network

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbMoviesService {

  @GET("movie/{movie_id}")
  suspend fun getMovieDetails(
    @Path("movie_id") movieId: Int,
    @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
  ): TmdbMovieDetailsResponse

  @GET("trending/movie/{list_type}")
  suspend fun getMovieList(
    @Path("list_type") listType: String,
    @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
  ): NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>
}
