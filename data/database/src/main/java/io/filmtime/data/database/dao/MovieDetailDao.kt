package io.filmtime.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.filmtime.data.database.entity.MovieDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailDao {

  @Query("SELECT * FROM movie_detail WHERE tmdbId = :tmdbId")
  fun getMovieByTmdbId(tmdbId: Int): Flow<MovieDetailEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun storeMovie(movie: MovieDetailEntity)

  @Query("DELETE FROM movie_detail")
  suspend fun deleteAll()
}
