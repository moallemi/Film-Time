package io.filmtime.data.network

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

@Serializable
data class TmdbMovieDetailsResponse (
  val adult: Boolean? = null,

  @SerialName("backdrop_path")
  val backdropPath: String? = null,

  @SerialName("belongs_to_collection")
  val belongsToCollection: JsonElement? = null,

  val budget: Long? = null,
  val genres: List<Genre>? = null,
  val homepage: String? = null,
  val id: Int? = null,

  @SerialName("imdb_id")
  val imdbID: String? = null,

  @SerialName("original_language")
  val originalLanguage: String? = null,

  @SerialName("original_title")
  val originalTitle: String? = null,

  val overview: String? = null,
  val popularity: Double? = null,

  @SerialName("poster_path")
  val posterPath: String? = null,

  @SerialName("production_companies")
  val productionCompanies: List<ProductionCompany>? = null,

  @SerialName("production_countries")
  val productionCountries: List<ProductionCountry>? = null,

  @SerialName("release_date")
  val releaseDate: String? = null,

  val revenue: Long? = null,
  val runtime: Long? = null,

  @SerialName("spoken_languages")
  val spokenLanguages: List<SpokenLanguage>? = null,

  val status: String? = null,
  val tagline: String? = null,
  val title: String? = null,
  val video: Boolean? = null,

  @SerialName("vote_average")
  val voteAverage: Double? = null,

  @SerialName("vote_count")
  val voteCount: Long? = null
)

@Serializable
data class Genre (
  val id: Long? = null,
  val name: String? = null
)

@Serializable
data class ProductionCompany (
  val id: Long? = null,

  @SerialName("logo_path")
  val logoPath: String? = null,

  val name: String? = null,

  @SerialName("origin_country")
  val originCountry: String? = null
)

@Serializable
data class ProductionCountry (
  @SerialName("iso_3166_1")
  val iso3166_1: String? = null,

  val name: String? = null
)

@Serializable
data class SpokenLanguage (
  @SerialName("english_name")
  val englishName: String? = null,

  @SerialName("iso_639_1")
  val iso639_1: String? = null,

  val name: String? = null
)

