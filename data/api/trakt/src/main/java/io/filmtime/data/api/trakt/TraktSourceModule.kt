package io.filmtime.data.api.trakt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class TraktSourceModule {

  @Binds
  abstract fun bindsTraktAuthRemoteSource(
    sourceImpl: TraktAuthRemoteSourceImpl
  ): TraktAuthRemoteSource

  @Binds
  abstract fun bindsTraktSearchRemoteSource(
    sourceImpl: TraktSearchRemoteSourceImpl
  ): TraktSearchRemoteSource
}
