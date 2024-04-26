package io.filmtime.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDetailDao {

  @Query("SELECT * FROM MovieDetailEntity WHERE traktId = :traktId")
  suspend fun getMovieByTraktId(traktId: Int): MovieDetailEntity?

  @Query("SELECT * FROM MovieDetailEntity WHERE tmdbId = :tmdbId")
  suspend fun getMovieByTmdbId(tmdbId: Int): MovieDetailEntity?

  @Insert
  suspend fun storeMovie(movie: MovieDetailEntity)
}
