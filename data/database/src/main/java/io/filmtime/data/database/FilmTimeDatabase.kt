package io.filmtime.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieDetailEntity::class], version = 1)
abstract class FilmTimeDatabase : RoomDatabase() {

  abstract fun movieDao(): MovieDetailDao
}
