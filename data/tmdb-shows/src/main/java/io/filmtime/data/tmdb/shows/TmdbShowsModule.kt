package io.filmtime.data.tmdb.shows

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbShowsModule {

  @Binds
  abstract fun bindTmdbShowsRemoteSource(impl: TmdbShowsRepositoryImpl): TmdbShowsRepository
}
