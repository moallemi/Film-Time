package io.filmtime.data.trakt.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class TraktAuthModule {

  @Binds
  abstract fun bindsTraktAuthRepository(
    impl: TraktAuthRepositoryImpl,
  ): TraktAuthRepository
}
