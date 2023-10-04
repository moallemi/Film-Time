package io.filmtime.data.network


import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

@Serializable
data class TraktAccessTokenResponse (
  @SerialName("access_token")
  val accessToken: String,

  @SerialName("token_type")
  val tokenType: String,

  @SerialName("expires_in")
  val expiresIn: Long,

  @SerialName("refresh_token")
  val refreshToken: String,

  val scope: String,

  @SerialName("created_at")
  val createdAt: Long
)
