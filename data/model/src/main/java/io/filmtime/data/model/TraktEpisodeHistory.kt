package io.filmtime.data.model

data class TraktEpisodeHistory(
  val traktId: Int,
  val seasonNumber: Int,
  val episodeNumber: Int,
  val isWatched: Boolean = false,
)
