package io.filmtime.domain.tmdb.movies

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbMoviesUseCaseModule {

  @Binds
  abstract fun bindGetMovieDetailsUseCase(
    impl: GetMovieDetailsUseCaseImpl,
  ): GetMovieDetailsUseCase

  @Binds
  abstract fun bindGetTrendingMoviesUseCase(
    impl: GetTrendingMoviesUseCaseImpl,
  ): GetTrendingMoviesUseCase
}
