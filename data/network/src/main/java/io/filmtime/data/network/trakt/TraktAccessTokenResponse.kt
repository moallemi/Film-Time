package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktAccessTokenResponse(
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
  val createdAt: Long,
)
