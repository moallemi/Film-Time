package io.filmtime.data.bookmarks

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BookmarksModule {

  @Binds
  internal abstract fun bindBookmarksRepository(impl: BookmarksRepositoryImpl): BookmarksRepository
}
