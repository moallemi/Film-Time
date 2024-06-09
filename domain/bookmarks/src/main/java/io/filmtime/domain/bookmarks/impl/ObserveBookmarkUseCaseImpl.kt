package io.filmtime.domain.bookmarks.impl

import io.filmtime.data.bookmarks.BookmarksRepository
import io.filmtime.data.model.VideoType
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveBookmarkUseCaseImpl @Inject constructor(
  private val bookmarksRepository: BookmarksRepository,
) : ObserveBookmarkUseCase {

  override suspend fun invoke(tmdbId: Int, type: VideoType): Flow<Boolean> =
    bookmarksRepository.isBookmarked(tmdbId, type)
}
