package io.filmtime.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import io.filmtime.data.model.VideoType

@Entity(
  tableName = "bookmarks",
  primaryKeys = ["tmdb_id", "video_type"],
)
data class BookmarkEntity(
  @ColumnInfo(name = "tmdb_id") val tmdbId: Int,
  @ColumnInfo(name = "video_type") val videoType: VideoType,
)
