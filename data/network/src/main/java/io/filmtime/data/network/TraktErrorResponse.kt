package io.filmtime.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktErrorResponse(
  val error: String,

  @SerialName("error_description")
  val errorDescription: String,
)
