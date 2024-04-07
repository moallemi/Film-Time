package io.filmtime.data.model

data class VideoDetail(
  val ids: VideoId,
  val title: String,
  val posterUrl: String,
  val coverUrl: String,
  val year: Int,
  val genres: List<String?>,
  val originalLanguage: String?,
  val spokenLanguages: List<String>,
  val description: String,
  val isWatched: Boolean? = null,
  val runtime: String?,
  val releaseDate: String,
  val voteAverage: Float = 0.0F,
)
