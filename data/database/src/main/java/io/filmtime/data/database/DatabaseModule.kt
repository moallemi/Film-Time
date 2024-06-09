package io.filmtime.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.filmtime.data.database.dao.BookmarksDao
import io.filmtime.data.database.dao.MovieDetailDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

  @Singleton
  @Provides
  fun providesDatabase(@ApplicationContext context: Context): FilmTimeDatabase {
    return Room.databaseBuilder(context, FilmTimeDatabase::class.java, "film-time-db")
      .build()
  }

  @Provides
  @Singleton
  fun providesMovieDetailDao(db: FilmTimeDatabase): MovieDetailDao = db.movieDao()

  @Provides
  @Singleton
  fun providesBookmarksDao(db: FilmTimeDatabase): BookmarksDao = db.bookmarksDao()
}
