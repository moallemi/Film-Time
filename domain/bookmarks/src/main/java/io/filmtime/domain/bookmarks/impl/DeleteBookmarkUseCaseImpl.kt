package io.filmtime.domain.bookmarks.impl

import io.filmtime.data.bookmarks.BookmarksRepository
import io.filmtime.data.model.VideoType
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import javax.inject.Inject

internal class DeleteBookmarkUseCaseImpl @Inject constructor(
  private val bookmarksRepository: BookmarksRepository,
) : DeleteBookmarkUseCase {

  override suspend fun invoke(tmdbId: Int, type: VideoType) {
    bookmarksRepository.removeBookmark(tmdbId, type)
  }
}
