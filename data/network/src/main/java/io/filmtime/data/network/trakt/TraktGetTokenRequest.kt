package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class TraktGetTokenRequest(
  val code: String,

  @SerialName("client_id")
  val clientID: String,

  @SerialName("client_secret")
  val clientSecret: String,

  @SerialName("redirect_uri")
  val redirectURI: String,

  @SerialName("grant_type")
  val grantType: String,
)
