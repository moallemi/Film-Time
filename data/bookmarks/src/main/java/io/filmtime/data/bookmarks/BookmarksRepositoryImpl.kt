package io.filmtime.data.bookmarks

import io.filmtime.data.database.dao.BookmarksDao
import io.filmtime.data.database.entity.BookmarkEntity
import io.filmtime.data.model.VideoType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class BookmarksRepositoryImpl @Inject constructor(
  private val bookmarksDao: BookmarksDao,
) : BookmarksRepository {

  override suspend fun addBookmark(tmdbId: Int, type: VideoType) {
    bookmarksDao.addBookmark(
      BookmarkEntity(
        tmdbId = tmdbId,
        videoType = type,
      ),
    )
  }

  override suspend fun removeBookmark(tmdbId: Int, type: VideoType) {
    bookmarksDao.removeBookmark(
      BookmarkEntity(
        tmdbId = tmdbId,
        videoType = type,
      ),
    )
  }

  override suspend fun isBookmarked(tmdbId: Int, type: VideoType): Flow<Boolean> =
    bookmarksDao.isBookmarked(tmdbId, type)
}
