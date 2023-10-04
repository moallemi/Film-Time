package io.filmtime.data.network

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class TraktGetTokenRequest (
  val code: String,

  @SerialName("client_id")
  val clientID: String,

  @SerialName("client_secret")
  val clientSecret: String,

  @SerialName("redirect_uri")
  val redirectURI: String,

  @SerialName("grant_type")
  val grantType: String
)
