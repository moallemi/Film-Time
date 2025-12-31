package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktClientIDRequest(
  @SerialName("client_id")
  val clientId: String,
)
