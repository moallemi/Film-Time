package io.filmtime.domain.trakt.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.trakt.auth.impl.GetTraktAccessTokenUseCaseImpl
import io.filmtime.domain.trakt.auth.impl.GetTraktAuthStateUseCaseImpl
import io.filmtime.domain.trakt.auth.impl.LogoutTraktUseCaseImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TraktAuthModule {

  @Binds
  abstract fun bindGetAccessTokenUseCase(impl: GetTraktAccessTokenUseCaseImpl): GetTraktAccessTokenUseCase

  @Binds
  abstract fun bindsGetTraktAuthStateUseCase(impl: GetTraktAuthStateUseCaseImpl): GetTraktAuthStateUseCase

  @Binds
  abstract fun bindsLogoutTraktUseCase(impl: LogoutTraktUseCaseImpl): LogoutTraktUseCase
}
