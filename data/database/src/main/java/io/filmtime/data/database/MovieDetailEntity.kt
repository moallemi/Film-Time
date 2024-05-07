package io.filmtime.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_detail")
data class MovieDetailEntity(
  @PrimaryKey
  val tmdbId: Int,
  val name: String,
  val posterUrl: String,
  val coverUrl: String,
  val year: Int,
  val description: String,
  val releaseDate: String,
  val runtime: String?,
  val originalLanguage: String?,
  val spokenLanguages: List<String>,
  val genres: List<String>,
  val voteAverage: Float,
)
