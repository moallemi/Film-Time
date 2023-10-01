package io.filmtime.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class TmdbShowDetailsResponse(
  val adult: Boolean? = null,

  @SerialName("backdrop_path")
  val backdropPath: String? = null,

  @SerialName("created_by")
  val createdBy: List<CreatedBy>? = null,

  @SerialName("episode_run_time")
  val episodeRunTime: List<Long>? = null,

  @SerialName("first_air_date")
  val firstAirDate: String? = null,

  val genres: List<Genre>? = null,
  val homepage: String? = null,
  val id: Int? = null,

  @SerialName("in_production")
  val inProduction: Boolean? = null,

  val languages: List<String>? = null,

  @SerialName("last_air_date")
  val lastAirDate: String? = null,

  @SerialName("last_episode_to_air")
  val lastEpisodeToAir: LastEpisodeToAir? = null,

  val name: String? = null,

  @SerialName("next_episode_to_air")
  val nextEpisodeToAir: JsonElement? = null,

  val networks: List<Network>? = null,

  @SerialName("number_of_episodes")
  val numberOfEpisodes: Long? = null,

  @SerialName("number_of_seasons")
  val numberOfSeasons: Long? = null,

  @SerialName("origin_country")
  val originCountry: List<String>? = null,

  @SerialName("original_language")
  val originalLanguage: String? = null,

  @SerialName("original_name")
  val originalName: String? = null,

  val overview: String? = null,
  val popularity: Double? = null,

  @SerialName("poster_path")
  val posterPath: String? = null,

  @SerialName("production_companies")
  val productionCompanies: List<Network>? = null,

  @SerialName("production_countries")
  val productionCountries: List<ProductionCountry>? = null,

  val seasons: List<Season>? = null,

  @SerialName("spoken_languages")
  val spokenLanguages: List<SpokenLanguage>? = null,

  val status: String? = null,
  val tagline: String? = null,
  val type: String? = null,

  @SerialName("vote_average")
  val voteAverage: Double? = null,

  @SerialName("vote_count")
  val voteCount: Long? = null,
)

@Serializable
data class CreatedBy(
  val id: Long? = null,

  @SerialName("credit_id")
  val creditID: String? = null,

  val name: String? = null,
  val gender: Long? = null,

  @SerialName("profile_path")
  val profilePath: String? = null,
)

@Serializable
data class LastEpisodeToAir(
  val id: Long? = null,
  val name: String? = null,
  val overview: String? = null,

  @SerialName("vote_average")
  val voteAverage: Double? = null,

  @SerialName("vote_count")
  val voteCount: Long? = null,

  @SerialName("air_date")
  val airDate: String? = null,

  @SerialName("episode_number")
  val episodeNumber: Long? = null,

  @SerialName("production_code")
  val productionCode: String? = null,

  val runtime: Long? = null,

  @SerialName("season_number")
  val seasonNumber: Long? = null,

  @SerialName("show_id")
  val showID: Long? = null,

  @SerialName("still_path")
  val stillPath: String? = null,
)

@Serializable
data class Network(
  val id: Long? = null,

  @SerialName("logo_path")
  val logoPath: String? = null,

  val name: String? = null,

  @SerialName("origin_country")
  val originCountry: String? = null,
)

@Serializable
data class Season(
  @SerialName("air_date")
  val airDate: String? = null,

  @SerialName("episode_count")
  val episodeCount: Long? = null,

  val id: Long? = null,
  val name: String? = null,
  val overview: String? = null,

  @SerialName("poster_path")
  val posterPath: String? = null,

  @SerialName("season_number")
  val seasonNumber: Long? = null,

  @SerialName("vote_average")
  val voteAverage: Double? = null,
)
