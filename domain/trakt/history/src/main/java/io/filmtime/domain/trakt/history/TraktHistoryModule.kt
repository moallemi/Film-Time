package io.filmtime.domain.trakt.history

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.trakt.history.impl.AddToHistoryUseCaseImpl
import io.filmtime.domain.trakt.history.impl.IsMovieWatchedUseCaseImpl
import io.filmtime.domain.trakt.history.impl.RemoveFromHistoryUseCaseImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TraktHistoryModule {

  @Binds
  internal abstract fun bindIsMovieWatchedUseCase(impl: IsMovieWatchedUseCaseImpl): IsMovieWatchedUseCase

  @Binds
  internal abstract fun bindAddToHistoryUseCase(impl: AddToHistoryUseCaseImpl): AddToHistoryUseCase

  @Binds
  internal abstract fun bindRemoveFromHistoryUseCase(impl: RemoveFromHistoryUseCaseImpl): RemoveFromHistoryUseCase
}
