package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktWatched(
  val plays: Long,

  @SerialName("last_watched_at")
  val lastWatchedAt: String,

  @SerialName("last_updated_at")
  val lastUpdatedAt: String,

  val movie: Movie
)

