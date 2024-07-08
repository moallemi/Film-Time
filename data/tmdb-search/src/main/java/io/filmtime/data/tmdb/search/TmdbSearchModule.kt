package io.filmtime.data.tmdb.search

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbSearchModule {

  @Binds
  abstract fun bindsTmdbSearchRepository(impl: TmdbSearchRepositoryImpl): TmdbSearchRepository
}
