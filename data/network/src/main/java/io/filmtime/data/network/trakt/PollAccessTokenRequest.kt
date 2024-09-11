package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PollAccessTokenRequest(
  val code: String,

  @SerialName("client_id")
  val clientID: String,

  @SerialName
  ("client_secret")
  val clientSecret: String,
)
