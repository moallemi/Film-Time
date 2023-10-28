package io.filmtime.data.trakt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class TraktModule {

  @Binds
  abstract fun bindsTraktHistoryRepository(
    impl: TraktHistoryRepositoryImpl,
  ): TraktHistoryRepository
}
