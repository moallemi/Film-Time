package io.filmtime.data.bookmarks

import io.filmtime.data.model.VideoType
import kotlinx.coroutines.flow.Flow

interface BookmarksRepository {

  suspend fun addBookmark(tmdbId: Int, type: VideoType)

  suspend fun removeBookmark(tmdbId: Int, type: VideoType)

  suspend fun isBookmarked(tmdbId: Int, type: VideoType): Flow<Boolean>
}
