package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncHistoryRequest(
  val movies: List<MovieHistory>? = null,
  val shows: List<ShowHistory>? = null,
)

@Serializable
data class MovieHistory(
  @SerialName("watched_at")
  val watchedAt: String? = null,
  val title: String? = null,
  val year: Long? = null,
  val ids: HistoryIDS,
)

@Serializable
data class ShowHistory(
  val ids: HistoryIDS,
  val seasons: List<SeasonHistory>,
)

@Serializable
data class SeasonHistory(
  val number: Int,
  val episodes: List<EpisodeHistory>,
)

@Serializable
data class EpisodeHistory(
  @SerialName("watched_at")
  val watchedAt: String?,
  val number: Int,
)

@Serializable
data class HistoryIDS(
  val trakt: Int? = null,
  val tvdb: Int? = null,
  val imdb: String? = null,
  val tmdb: Int? = null,
  val slug: String? = null,
)

@Serializable
data class Show(
  val title: String,
  val year: Long,
  val ids: HistoryIDS,
  val seasons: List<Season>? = null,
)

@Serializable
data class Episode(
  val ids: HistoryIDS,
  val season: Int,
  val number: Int,
)

@Serializable
data class Season(
  @SerialName("watched_at")
  val watchedAt: String? = null,

  val number: Long,
  val episodes: List<SeasonEpisode>? = null,
)

@Serializable
data class SeasonEpisode(
  @SerialName("watched_at")
  val watchedAt: String? = null,

  val number: Long,
)
