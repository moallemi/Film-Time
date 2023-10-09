package io.filmtime.data.network.trakt

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.filmtime.data.network.BuildConfig
import io.filmtime.data.network.adapter.NetworkResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface TraktSyncService {

  @GET("sync/history/{type}/{id}")
  @Headers(
    "trakt-api-key: ${BuildConfig.TRAKT_CLIENT_ID}",
    "trakt-api-version: 2",
  )
  suspend fun getWatchedHistory(
    @Path("type") type: String,
    @Path("id") id: String,
    @Header("Authorization") accessToken: String,
  ): NetworkResponse<Nothing, TraktErrorResponse>
}

suspend fun main() {

  val r = Retrofit.Builder()
    .baseUrl("https://api.trakt.tv")
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(MediaType.parse("application/json")!!))

    .build()
  val c = r.create(TraktSyncService::class.java)




}
