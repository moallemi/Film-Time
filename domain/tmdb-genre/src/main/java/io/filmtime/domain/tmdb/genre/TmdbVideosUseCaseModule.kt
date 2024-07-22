package io.filmtime.domain.tmdb.genre

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TmdbVideosUseCaseModule {

  @Binds
  abstract fun bindsTmdbVideosGenreUseCase(impl: GetVideosByGenreUseCaseImpl): GetVideosByGenreUseCase
}
