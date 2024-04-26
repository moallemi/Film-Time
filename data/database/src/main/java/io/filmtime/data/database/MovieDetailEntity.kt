package io.filmtime.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDetailEntity(
  @PrimaryKey
  val tmdbId: Int,
  val traktId: Int?,
  val name: String,
  val posterUrl: String,
  val coverUrl: String,
  val year: Int,
//  val genres: List<String>,
  val description: String,
  val releaseDate: String,
  val isWatched: Boolean = false,
  val runtime: String?,
  val voteAverage: Float = 0.0F,
  val voteColor: Long = 0,
  val originalLanguage: String?,
//  val spokenLanguages: List<String>,
)

// val ids: VideoId,
//  val title: String,
//  val posterUrl: String,
//  val coverUrl: String,
//  val year: Int,
//  val genres: List<String?>,
//  val originalLanguage: String?,
//  val spokenLanguages: List<String>,
//  val description: String,
//  val isWatched: Boolean? = null,
//  val runtime: String?,
//  val releaseDate: String,
//  val voteAverage: Float = 0.0F,
//  val voteColor: Long = 0,
