package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktPersonResponse(
  val adult: Boolean,

  @SerialName("also_known_as")
  val alsoKnownAs: List<String>,

  val biography: String,
  val birthday: String,

  @SerialName("deathday")
  val deathDay: String? = null,
  val gender: Long,
  val homepage: String? = null,
  val id: Long,

  @SerialName("imdb_id")
  val imdbID: String,

  @SerialName("known_for_department")
  val knownForDepartment: String,

  val name: String,

  @SerialName("place_of_birth")
  val placeOfBirth: String,

  val popularity: Double,

  @SerialName("profile_path")
  val profilePath: String,
)
