package io.filmtime.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.filmtime.data.model.VideoType

@Entity(
  tableName = "bookmarks",
)
data class BookmarkEntity(
  @PrimaryKey
  val tmdbId: Int,
  val videoType: VideoType,
)
