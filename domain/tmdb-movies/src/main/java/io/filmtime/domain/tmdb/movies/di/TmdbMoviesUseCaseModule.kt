package io.filmtime.domain.tmdb.movies.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.tmdb.movies.GetMovieCreditUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import io.filmtime.domain.tmdb.movies.GetSimilarUseCase
import io.filmtime.domain.tmdb.movies.ObserveMoviesStreamUseCase
import io.filmtime.domain.tmdb.movies.impl.GetMovieCreditUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetMovieDetailsUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetMoviesListUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetSimilarUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.ObserveMoviesStreamUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbMoviesUseCaseModule {

  @Binds
  abstract fun bindGetMovieDetailsUseCase(impl: GetMovieDetailsUseCaseImpl): GetMovieDetailsUseCase

  @Binds
  abstract fun bindGetTrendingMoviesStream(impl: ObserveMoviesStreamUseCaseImpl): ObserveMoviesStreamUseCase

  @Binds
  abstract fun bindGetMoviesListUseCase(impl: GetMoviesListUseCaseImpl): GetMoviesListUseCase

  @Binds
  abstract fun bindGetMoviesCreditUseCase(impl: GetMovieCreditUseCaseImpl): GetMovieCreditUseCase

  @Binds
  abstract fun bindGetSimilarUseCase(impl: GetSimilarUseCaseImpl): GetSimilarUseCase
}
