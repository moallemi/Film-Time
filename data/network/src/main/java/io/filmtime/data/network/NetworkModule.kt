package io.filmtime.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.data.network.adapter.NetworkCallAdapterFactory
import io.filmtime.data.network.annotation.TmdbApi
import io.filmtime.data.network.annotation.TraktHeaderInterceptor
import io.filmtime.data.network.annotation.TraktNetwork
import io.filmtime.data.network.annotation.TraktOkHttp
import io.filmtime.data.network.interceptor.TmdbApiKeyInterceptor
import io.filmtime.data.network.interceptor.TraktHeadersInterceptor
import io.filmtime.data.network.trakt.TraktAuthService
import io.filmtime.data.network.trakt.TraktSearchService
import io.filmtime.data.network.trakt.TraktService
import io.filmtime.data.network.trakt.TraktSyncService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun providesJson(): Json {
    return Json {
      ignoreUnknownKeys = true
    }
  }

  @TmdbApi
  @Provides
  @Singleton
  fun providesTmdbInterceptor(): Interceptor = TmdbApiKeyInterceptor()

  @Provides
  @Singleton
  @TraktHeaderInterceptor
  fun providesTraktHeadersInterceptor(): Interceptor = TraktHeadersInterceptor()

  @Provides
  @Singleton
  fun providesTmdbOkHttpClient(@TmdbApi interceptor: Interceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder.addInterceptor(interceptor)

    return builder.build()
  }

  @Provides
  @Singleton
  fun providesRetrofit(
    json: Json,
    networkCallAdapterFactory: CallAdapter.Factory,
    okHttpClient: OkHttpClient,
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.themoviedb.org/3/")
      .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
      .addCallAdapterFactory(networkCallAdapterFactory)
      .client(okHttpClient)
      .build()
  }

  @Provides
  @Singleton
  @TraktNetwork
  fun providesTraktRetrofit(
    json: Json,
    networkCallAdapterFactory: CallAdapter.Factory,
    @TraktOkHttp client: OkHttpClient,
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.trakt.tv/")
      .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
      .addCallAdapterFactory(networkCallAdapterFactory)
      .client(client)
      .build()
  }

  @Provides
  @Singleton
  @TraktOkHttp
  fun providesTraktOkHttp(
    @TraktHeaderInterceptor headerInterceptor: Interceptor,
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(headerInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun providesTmdbMovieService(retrofit: Retrofit): TmdbMoviesService {
    return retrofit.create(TmdbMoviesService::class.java)
  }

  @Provides
  @Singleton
  fun providesTmdbShowsService(retrofit: Retrofit): TmdbShowsService {
    return retrofit.create(TmdbShowsService::class.java)
  }

  @Provides
  @Singleton
  fun providesTraktAuthService(@TraktNetwork retrofit: Retrofit): TraktAuthService {
    return retrofit.create(TraktAuthService::class.java)
  }

  @Provides
  @Singleton
  fun providesTraktIDLookupService(@TraktNetwork retrofit: Retrofit): TraktSearchService {
    return retrofit.create(TraktSearchService::class.java)
  }

  @Provides
  @Singleton
  fun provideTraktSync(@TraktNetwork retrofit: Retrofit): TraktSyncService {
    return retrofit.create(TraktSyncService::class.java)
  }

  @Provides
  @Singleton
  fun provideTrakt(@TraktNetwork retrofit: Retrofit): TraktService =
    retrofit.create(TraktService::class.java)

  @Provides
  @Singleton
  fun providesTmdbSearchService(retrofit: Retrofit): TmdbSearchService {
    return retrofit.create(TmdbSearchService::class.java)
  }

  @Provides
  @Singleton
  fun providesNetworkCallAdapterFactory(): CallAdapter.Factory {
    return NetworkCallAdapterFactory()
  }

  @Provides
  @Singleton
  fun providesCollectionService(retrofit: Retrofit): TmdbCollectionService =
    retrofit.create(TmdbCollectionService::class.java)
}
