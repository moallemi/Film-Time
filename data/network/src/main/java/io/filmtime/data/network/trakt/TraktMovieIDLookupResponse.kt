package io.filmtime.data.network.trakt

import kotlinx.serialization.Serializable

@Serializable
data class TraktMovieIDLookupResponse(
  val type: String,
  val score: Long,
  val movie: Movie,
)

@Serializable
data class Movie(
  val title: String,
  val year: Long,
  val ids: IDS,
)

@Serializable
data class IDS(
  val trakt: Int,
  val slug: String,
  val imdb: String,
  val tmdb: Int,
)
