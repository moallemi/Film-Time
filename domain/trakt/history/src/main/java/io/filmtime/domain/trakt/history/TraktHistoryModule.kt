package io.filmtime.domain.trakt.history

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.trakt.history.impl.AddToHistoryUseCaseImpl
import io.filmtime.domain.trakt.history.impl.IsVideoWatchedUseCaseImpl
import io.filmtime.domain.trakt.history.impl.RemoveFromHistoryUseCaseImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TraktHistoryModule {

  @Binds
  internal abstract fun bindIsVideoWatchedUseCase(impl: IsVideoWatchedUseCaseImpl): IsVideoWatchedUseCase

  @Binds
  internal abstract fun bindAddToHistoryUseCase(impl: AddToHistoryUseCaseImpl): AddToHistoryUseCase

  @Binds
  internal abstract fun bindRemoveFromHistoryUseCase(impl: RemoveFromHistoryUseCaseImpl): RemoveFromHistoryUseCase
}
