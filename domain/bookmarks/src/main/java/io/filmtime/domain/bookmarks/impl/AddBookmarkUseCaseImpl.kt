package io.filmtime.domain.bookmarks.impl

import io.filmtime.data.bookmarks.BookmarksRepository
import io.filmtime.data.model.VideoType
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import javax.inject.Inject

internal class AddBookmarkUseCaseImpl @Inject constructor(
  private val bookmarksRepository: BookmarksRepository,
) : AddBookmarkUseCase {

  override suspend fun invoke(tmdbId: Int, type: VideoType) {
    bookmarksRepository.addBookmark(tmdbId, type)
  }
}
