package io.filmtime.domain.trakt.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.trakt.auth.GetTraktAccessTokenByCodeUseCase
import io.filmtime.domain.trakt.auth.GetTraktAccessTokenUseCase
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import io.filmtime.domain.trakt.auth.GetTraktLoginCodeUseCase
import io.filmtime.domain.trakt.auth.LogoutTraktUseCase
import io.filmtime.domain.trakt.auth.impl.GetTraktAccessTokenByCodeUseCaseImpl
import io.filmtime.domain.trakt.auth.impl.GetTraktAccessTokenUseCaseImpl
import io.filmtime.domain.trakt.auth.impl.GetTraktAuthStateUseCaseImpl
import io.filmtime.domain.trakt.auth.impl.GetTraktLoginCodeUseCaseImpl
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

  @Binds
  abstract fun bindsGetTraktLoginCodeUseCase(impl: GetTraktLoginCodeUseCaseImpl): GetTraktLoginCodeUseCase

  @Binds
  abstract fun bindsGetTraktAccessTokenByCodeUseCase(
    impl: GetTraktAccessTokenByCodeUseCaseImpl,
  ): GetTraktAccessTokenByCodeUseCase
}
