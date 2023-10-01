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
}
