package io.filmtime.domain.bookmarks.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.bookmarks.impl.AddBookmarkUseCaseImpl
import io.filmtime.domain.bookmarks.impl.DeleteBookmarkUseCaseImpl
import io.filmtime.domain.bookmarks.impl.ObserveBookmarkUseCaseImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class BookmarksModule {

  @Binds
  abstract fun bindAddBookmarkUseCase(impl: AddBookmarkUseCaseImpl): AddBookmarkUseCase

  @Binds
  abstract fun bindRemoveBookmarkUseCase(impl: DeleteBookmarkUseCaseImpl): DeleteBookmarkUseCase

  @Binds
  abstract fun bindObserveBookmarkUseCase(impl: ObserveBookmarkUseCaseImpl): ObserveBookmarkUseCase
}
