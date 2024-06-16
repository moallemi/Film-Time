package io.filmtime.data.network.trakt

import kotlinx.serialization.Serializable

@Serializable
data class SyncHistoryResponse(
  val added: SyncHistoryDto? = null,
  val deleted: SyncHistoryDto? = null,
)

@Serializable
data class SyncHistoryDto(
  val movies: Long,
  val episodes: Long,
)
