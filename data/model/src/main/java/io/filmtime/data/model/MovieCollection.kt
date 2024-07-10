package io.filmtime.data.model

data class MovieCollection(
  val id: Long,
  val name: String,
  val overview: String,
  val posterPath: String,
  val backdropPath: String,
  val parts: List<VideoThumbnail>,
)
