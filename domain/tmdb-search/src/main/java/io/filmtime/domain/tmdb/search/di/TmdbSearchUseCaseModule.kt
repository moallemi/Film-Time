package io.filmtime.domain.tmdb.search.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.tmdb.search.SearchTmdbUseCase
import io.filmtime.domain.tmdb.search.impl.SearchTmdbUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbSearchUseCaseModule {

  @Binds
  abstract fun bindsSearchUseCase(impl: SearchTmdbUseCaseImpl): SearchTmdbUseCase
}
