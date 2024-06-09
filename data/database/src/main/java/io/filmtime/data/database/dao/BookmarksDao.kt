package io.filmtime.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.filmtime.data.database.entity.BookmarkEntity
import io.filmtime.data.model.VideoType
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarksDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addBookmark(bookmarkEntity: BookmarkEntity)

  @Delete
  suspend fun removeBookmark(bookmarkEntity: BookmarkEntity)

  @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE tmdbId = :tmdbId AND videoType = :type)")
  fun isBookmarked(tmdbId: Int, type: VideoType): Flow<Boolean>
}
