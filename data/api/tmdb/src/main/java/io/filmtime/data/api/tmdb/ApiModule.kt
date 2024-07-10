package io.filmtime.data.api.tmdb

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ApiModule {

  @Binds
  abstract fun bindTmdbMovieService(impl: TmdbMoviesRemoteSourceImpl): TmdbMoviesRemoteSource

  @Binds
  abstract fun bindTmdbShowService(impl: TmdbShowsRemoteSourceImpl): TmdbShowsRemoteSource

  @Binds
  abstract fun bindTmdbPersonSource(impl: TmdbPersonRemoteSourceImpl): TmdbPersonRemoteSource
}
