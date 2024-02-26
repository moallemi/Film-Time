package io.filmtime.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbCreditsResponse(
  val id: Long? = null,
  val cast: List<Cast>? = null,
  val crew: List<Cast>? = null,
)

@Serializable
data class Cast(
  val adult: Boolean? = null,
  val gender: Long? = null,
  val id: Long? = null,

  @SerialName("known_for_department")
  val knownForDepartment: String? = null,

  val name: String? = null,

  @SerialName("original_name")
  val originalName: String? = null,

  val popularity: Double? = null,

  @SerialName("profile_path")
  val profilePath: String? = null,

  @SerialName("cast_id")
  val castID: Long? = null,

  val character: String? = null,

  @SerialName("credit_id")
  val creditID: String? = null,

  val order: Long? = null,
  val department: String? = null,
  val job: String? = null,
)

