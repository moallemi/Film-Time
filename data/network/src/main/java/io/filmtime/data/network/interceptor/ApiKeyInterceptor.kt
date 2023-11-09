package io.filmtime.data.network.interceptor

import io.filmtime.data.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val originalHttpUrl = chain.request().url()

    val newHttpUrl = originalHttpUrl
      .newBuilder()
      .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
      .build()

    val requestBuilder = originalRequest
      .newBuilder()
      .url(newHttpUrl)

    val newRequest = requestBuilder.build()
    return chain.proceed(newRequest)
  }
}
