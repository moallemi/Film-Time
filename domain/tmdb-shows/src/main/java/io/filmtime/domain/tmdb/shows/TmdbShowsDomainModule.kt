package io.filmtime.domain.tmdb.shows

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbShowsDomainModule {

  @Binds
  abstract fun bindGetTrendingShowsUseCase(impl: GetTrendingShowsUseCaseImpl): GetTrendingShowsUseCase

  @Binds
  abstract fun bindGetShowDetailsUseCase(impl: GetShowDetailsUseCaseImpl): GetShowDetailsUseCase

  @Binds
  abstract fun bindObserveShowsStreamUseCase(impl: ObserveShowsStreamUseCaseImpl): ObserveShowsStreamUseCase

  @Binds
  abstract fun bindGetShowsListUseCase(impl: GetShowsListUseCaseImpl): GetShowsListUseCase

  @Binds
  abstract fun bindGetShowCredit(impl: GetShowCreditsUseCaseImpl): GetShowCreditsUseCase

  @Binds
  abstract fun bindGetSimilar(impl: GetSimilarUseCaseImpl): GetSimilarUseCase
}
