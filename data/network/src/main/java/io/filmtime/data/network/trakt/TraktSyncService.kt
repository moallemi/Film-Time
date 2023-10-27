package io.filmtime.data.network.trakt

import io.filmtime.data.network.BuildConfig
import io.filmtime.data.network.adapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface TraktSyncService {

  @GET("sync/watched/{type}")
  @Headers(
    "trakt-api-key: ${BuildConfig.TRAKT_CLIENT_ID}",
    "trakt-api-version: 2",
  )
  suspend fun getWatchedHistory(
    @Path("type") type: String,
    @Header("Authorization") accessToken: String,
  ): NetworkResponse<List<TraktWatched>, TraktErrorResponse>

  @GET("sync/history/{type}/{id}")
  @Headers(
    "trakt-api-key: ${BuildConfig.TRAKT_CLIENT_ID}",
    "trakt-api-version: 2",
  )
  suspend fun getHistoryById(
    @Path("type") type: String,
    @Path("id") id: String,
    @Header("Authorization") accessToken: String,
  ): NetworkResponse<List<HistoryMovie>, TraktErrorResponse>

  @GET("sync/history")
  @Headers(
    "trakt-api-key: ${BuildConfig.TRAKT_CLIENT_ID}",
    "trakt-api-version: 2",
  )
  suspend fun addMovieToHistory(
    @Header("Authorization") accessToken: String,
  ): NetworkResponse<List<HistoryMovie>, TraktErrorResponse>
}
