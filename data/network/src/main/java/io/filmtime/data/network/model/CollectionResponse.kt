package io.filmtime.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionResponse(
  val id: Long,
  val name: String,
  val overview: String,

  @SerialName("poster_path")
  val posterPath: String,

  @SerialName("backdrop_path")
  val backdropPath: String,

  val parts: List<Part>,
)

@Serializable
data class Part(
  @SerialName("backdrop_path")
  val backdropPath: String,

  val id: Long,
  val title: String,

  @SerialName("original_title")
  val originalTitle: String,

  val overview: String,

  @SerialName("poster_path")
  val posterPath: String? = null,

  @SerialName("media_type")
  val mediaType: String,

  val adult: Boolean,

  @SerialName("original_language")
  val originalLanguage: String,

  @SerialName("genre_ids")
  val genreIDS: List<Long>,

  val popularity: Double,

  @SerialName("release_date")
  val releaseDate: String,

  val video: Boolean,

  @SerialName("vote_average")
  val voteAverage: Double,

  @SerialName("vote_count")
  val voteCount: Long,
)
