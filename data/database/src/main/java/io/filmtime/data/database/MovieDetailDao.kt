package io.filmtime.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDetailDao {

  @Query("SELECT * FROM MovieDetailEntity WHERE tmdbId = :tmdbId")
  suspend fun getMovieByTmdbId(tmdbId: Int): MovieDetailEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun storeMovie(movie: MovieDetailEntity)

  @Query("DELETE FROM MovieDetailEntity")
  suspend fun deleteAll()
}
