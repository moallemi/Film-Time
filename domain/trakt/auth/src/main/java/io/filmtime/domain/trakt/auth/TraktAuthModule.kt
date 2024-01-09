package io.filmtime.domain.trakt.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class TraktAuthModule {

  @Binds
  abstract fun bindGetAccessTokenUseCase(
    getTraktAccessTokenUseCaseImpl: GetTraktAccessTokenUseCaseImpl,
  ): GetTraktAccessTokenUseCase

  @Binds
  abstract fun bindsGetTraktAuthStateUseCase(
    getTraktAuthStateUseCase: GetTraktAuthStateUseCaseImpl,
  ): GetTraktAuthStateUseCase
}
