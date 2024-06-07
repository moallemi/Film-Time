package io.filmtime.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.filmtime.data.database.dao.BookmarksDao
import io.filmtime.data.database.dao.MovieDetailDao
import io.filmtime.data.database.entity.BookmarkEntity
import io.filmtime.data.database.entity.MovieDetailEntity

@Database(
  entities = [
    MovieDetailEntity::class,
    BookmarkEntity::class,
  ],
  version = 1,
)
@TypeConverters(StringListConverter::class)
internal abstract class FilmTimeDatabase : RoomDatabase() {

  abstract fun movieDao(): MovieDetailDao

  abstract fun bookmarksDao(): BookmarksDao
}
