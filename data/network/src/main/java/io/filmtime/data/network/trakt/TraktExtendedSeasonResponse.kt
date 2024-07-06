package io.filmtime.data.network.trakt

import kotlinx.serialization.Serializable

@Serializable
data class TraktExtendedSeasonResponse(
  val number: Int,
  val ids: HistoryIDS,
  val episodes: List<Episode>? = null,
)
