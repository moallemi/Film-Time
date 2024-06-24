package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktExtendedRatingsResponse(
  val trakt: Trakt? = null,
  val tmdb: Mdb? = null,
  val imdb: Mdb? = null,
  val metascore: Metascore? = null,

  @SerialName("rotten_tomatoes")
  val rottenTomatoes: RottenTomatoes? = null,
)

@Serializable
data class Mdb(
  val rating: Double? = null,
  val votes: Long? = null,
)

@Serializable
data class Metascore(
  val rating: Long? = null,
)

@Serializable
data class RottenTomatoes(
  val rating: Double? = null,

  @SerialName("user_rating")
  val userRating: Long? = null,
)

@Serializable
data class Trakt(
  val rating: Double? = null,
  val votes: Long? = null,
)
