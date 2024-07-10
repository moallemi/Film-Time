package io.filmtime.domain.tmdb.person.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.tmdb.person.GetPersonByIDUseCase
import io.filmtime.domain.tmdb.person.impl.GetPersonByIDUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class TmdbPersonDomainModule {

  @Binds
  internal abstract fun bindGetPersonUseCase(impl: GetPersonByIDUseCaseImpl): GetPersonByIDUseCase
}
