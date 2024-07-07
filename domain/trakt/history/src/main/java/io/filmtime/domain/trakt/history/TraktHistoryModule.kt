package io.filmtime.domain.trakt.history

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.trakt.history.impl.AddEpisodeToHistoryUseCaseImpl
import io.filmtime.domain.trakt.history.impl.AddMovieToHistoryUseCaseImpl
import io.filmtime.domain.trakt.history.impl.IsMovieWatchedUseCaseImpl
import io.filmtime.domain.trakt.history.impl.IsShowWatchedUseCaseImpl
import io.filmtime.domain.trakt.history.impl.RemoveEpisodeFromHistoryUseCaseImpl
import io.filmtime.domain.trakt.history.impl.RemoveMovieFromHistoryUseCaseImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TraktHistoryModule {

  @Binds
  internal abstract fun bindIsVideoWatchedUseCase(impl: IsMovieWatchedUseCaseImpl): IsMovieWatchedUseCase

  @Binds
  internal abstract fun bindAddToHistoryUseCase(impl: AddMovieToHistoryUseCaseImpl): AddMovieToHistoryUseCase

  @Binds
  internal abstract fun bindRemoveFromHistoryUseCase(
    impl: RemoveMovieFromHistoryUseCaseImpl,
  ): RemoveMovieFromHistoryUseCase

  @Binds
  internal abstract fun bindIsShowWatchedUseCase(impl: IsShowWatchedUseCaseImpl): IsShowWatchedUseCase

  @Binds
  internal abstract fun bindAddEpisodeToHistoryUseCase(impl: AddEpisodeToHistoryUseCaseImpl): AddEpisodeToHistoryUseCase

  @Binds
  internal abstract fun bindRemoveEpisodeFromHistoryUseCase(
    impl: RemoveEpisodeFromHistoryUseCaseImpl,
  ): RemoveEpisodeFromHistoryUseCase
}
