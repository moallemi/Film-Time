package io.filmtime.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MovieDetailEntity::class], version = 1)
@TypeConverters(StringListConverter::class)
internal abstract class FilmTimeDatabase : RoomDatabase() {

  abstract fun movieDao(): MovieDetailDao
}
