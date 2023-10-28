package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AddHistoryRequest (
  val movies: List<MovieHistory>? = null,
  val shows: List<Show>? = null,
)

@Serializable
data class MovieHistory (
  @SerialName("watched_at")
  val watchedAt: String? = null,
  val title: String? = null,
  val year: Long? = null,
  val ids: HistoryIDS
)

@Serializable
data class HistoryIDS (
  val trakt: Long? = null,
  val tvdb: Long? = null,
  val imdb: String? = null,
  val tmdb: Long? = null,
  val slug: String? = null
)

@Serializable
data class Show (
  val title: String,
  val year: Long,
  val ids: HistoryIDS,
  val seasons: List<Season>? = null
)

@Serializable
data class Season (
  @SerialName("watched_at")
  val watchedAt: String? = null,

  val number: Long,
  val episodes: List<SeasonEpisode>? = null
)


@Serializable
data class SeasonEpisode (
  @SerialName("watched_at")
  val watchedAt: String? = null,

  val number: Long
)
