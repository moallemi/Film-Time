package io.filmtime.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.data.network.adapter.NetworkCallAdapterFactory
import io.filmtime.data.network.trakt.TraktAuthService
import io.filmtime.data.network.trakt.TraktSearchService
import io.filmtime.data.network.trakt.TraktSyncService
import io.filmtime.data.network.annotation.TmdbApi
import io.filmtime.data.network.interceptor.TmdbApiKeyInterceptor
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import javax.inject.Qualifier
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
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.trakt.tv/")
      .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
      .addCallAdapterFactory(networkCallAdapterFactory)
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
  fun providesNetworkCallAdapterFactory(): CallAdapter.Factory {
    return NetworkCallAdapterFactory()
  }
}

@Qualifier
@Target(
  AnnotationTarget.FUNCTION,
  AnnotationTarget.PROPERTY_GETTER,
  AnnotationTarget.PROPERTY_SETTER,
  AnnotationTarget.FIELD,
  AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.BINARY)
annotation class TraktNetwork
