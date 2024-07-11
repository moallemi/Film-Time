package io.filmtime.domain.tmdb.movies.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.tmdb.movies.GetMovieCollectionUseCase
import io.filmtime.domain.tmdb.movies.GetMovieCreditsUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import io.filmtime.domain.tmdb.movies.GetSimilarMoviesUseCase
import io.filmtime.domain.tmdb.movies.ObserveMoviesStreamUseCase
import io.filmtime.domain.tmdb.movies.impl.GetMovieCollectionUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetMovieCreditsUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetMovieDetailsUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetMoviesListUseCaseImpl
import io.filmtime.domain.tmdb.movies.impl.GetSimilarMoviesUseCaseImpl
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
  abstract fun bindGetMovieCreditUseCase(impl: GetMovieCreditsUseCaseImpl): GetMovieCreditsUseCase

  @Binds
  abstract fun bindGetSimilarUseCase(impl: GetSimilarMoviesUseCaseImpl): GetSimilarMoviesUseCase

  @Binds
  abstract fun bindGetMovieCollectionUseCase(impl: GetMovieCollectionUseCaseImpl): GetMovieCollectionUseCase
}
