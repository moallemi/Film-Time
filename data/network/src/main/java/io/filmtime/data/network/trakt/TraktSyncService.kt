package io.filmtime.data.network.trakt

import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TraktSyncService {

  @GET("sync/watched/{type}")
  suspend fun getWatchedHistory(
    @Path("type") type: String,
    @Header("Authorization") accessToken: String,
  ): NetworkResponse<List<TraktWatched>, TraktErrorResponse>

  @GET("sync/history/{type}/{id}")
  suspend fun getHistoryById(
    @Path("type") type: String,
    @Path("id") id: Int,
    @Header("Authorization") accessToken: String,
    @Query("limit") limit: Int = 240,
  ): NetworkResponse<List<HistoryMovie>, TraktErrorResponse>

  @POST("sync/history")
  suspend fun addMovieToHistory(
    @Header("Authorization") accessToken: String,
    @Body body: AddHistoryRequest,
  ): NetworkResponse<AddToHistoryResponse, TraktErrorResponse>
}
