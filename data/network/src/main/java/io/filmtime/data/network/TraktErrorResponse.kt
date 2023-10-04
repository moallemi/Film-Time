package io.filmtime.data.network

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class TraktErrorResponse (
  val error: String,

  @SerialName("error_description")
  val errorDescription: String
)
