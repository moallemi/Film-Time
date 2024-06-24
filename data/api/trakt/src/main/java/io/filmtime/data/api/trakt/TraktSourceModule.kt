package io.filmtime.data.api.trakt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TraktSourceModule {

  @Binds
  abstract fun bindsTraktAuthRemoteSource(impl: TraktAuthRemoteSourceImpl): TraktAuthRemoteSource

  @Binds
  abstract fun bindsTraktSearchRemoteSource(impl: TraktSearchRemoteSourceImpl): TraktSearchRemoteSource

  @Binds
  abstract fun bindsTraktSyncRemoteSource(impl: TraktSyncRemoteSourceImpl): TraktSyncRemoteSource

  @Binds
  abstract fun bindsTraktUserRemoteSource(impl: TraktRemoteSourceImpl): TraktRemoteSource
}
