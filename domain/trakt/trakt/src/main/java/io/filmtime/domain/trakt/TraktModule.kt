package io.filmtime.domain.trakt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TraktModule {

  @Binds
  internal abstract fun bindGetRatingsUseCase(impl: GetRatingsUseCaseImpl): GetRatingsUseCase
}
