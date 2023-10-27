package io.filmtime.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TraktAuthInterceptor: Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val original = chain.request()
    val requestBuilder: Request.Builder = original.newBuilder()
      .header("Authorization", "Bearer ") // Add the token here

    val request: Request = requestBuilder.build()
    return chain.proceed(request)
  }
}
