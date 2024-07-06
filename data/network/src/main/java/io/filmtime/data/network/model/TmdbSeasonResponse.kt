package io.filmtime.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbSeasonResponse(
  @SerialName("season_number")
  val seasonNumber: Int,

  val episodes: List<TmdbEpisodeResponse>,
)

@Serializable
data class TmdbEpisodeResponse(
  @SerialName("episode_number")
  val episodeNumber: Int,

  @SerialName("season_number")
  val seasonNumber: Int,

  @SerialName("still_path")
  val stillPath: String?,

  @SerialName("air_date")
  val airDate: String?,

  val id: Int?,
  val name: String?,
  val overview: String?,
  val runtime: Int?,
)
