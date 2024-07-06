package io.filmtime.data.model

data class TraktMovieHistory(
  val traktId: Int,
  val isWatched: Boolean = false,
)
