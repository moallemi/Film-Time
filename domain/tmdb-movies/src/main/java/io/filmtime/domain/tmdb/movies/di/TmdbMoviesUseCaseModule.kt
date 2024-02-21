package io.filmtime.domain.tmdb.movies.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import io.filmtime.domain.tmdb.movies.GetTrendingMoviesUseCase
import io.filmtime.domain.tmdb.movies.GetUpcomingMoviesUseCase
import io.filmtime.domain.tmdb.movies.ObserveMoviesStreamUseCase
import io.filmtime.domain.tmdb.movies.impl.GetMovieDetailsUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetMoviesListUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetTrendingMoviesUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetUpcomingMoviesUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.ObserveMoviesStreamUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbMoviesUseCaseModule {

  @Binds
  abstract fun bindGetMovieDetailsUseCase(impl: GetMovieDetailsUseCaseImpl): GetMovieDetailsUseCase

  @Binds
  abstract fun bindGetTrendingMoviesUseCase(impl: GetTrendingMoviesUseCaseImpl): GetTrendingMoviesUseCase

  @Binds
  abstract fun bindGetTrendingMoviesStream(impl: ObserveMoviesStreamUseCaseImpl): ObserveMoviesStreamUseCase

  @Binds
  abstract fun bindGetUpcomingMoviesUseCase(impl: GetUpcomingMoviesUseCaseImpl): GetUpcomingMoviesUseCase

  @Binds
  abstract fun bindGetMoviesListUseCase(impl: GetMoviesListUseCaseImpl): GetMoviesListUseCase
}
