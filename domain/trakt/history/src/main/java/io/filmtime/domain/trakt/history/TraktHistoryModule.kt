package io.filmtime.domain.trakt.history

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TraktHistoryModule {

  @Binds
  internal abstract fun bindIsMovieWatchedUseCase(impl: IsMovieWatchedUseCaseImpl): IsMovieWatchedUseCase

  @Binds
  internal abstract fun bindAddToHistoryUseCase(impl: AddToHistoryUseCaseImpl): AddToHistoryUseCase
}
