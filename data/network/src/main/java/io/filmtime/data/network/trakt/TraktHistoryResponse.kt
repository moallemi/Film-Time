package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class TraktHistoryResponse(
  val id: Long,

  @SerialName("watched_at")
  val watchedAt: String,

  val action: String,
  val type: String,
  val movie: Movie? = null,
  val show: Show? = null,
  val episode: Episode? = null,
)
