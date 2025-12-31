package io.filmtime.data.network.trakt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktDeviceCodeResponse(
  @SerialName("device_code")
  val deviceCode: String,

  @SerialName("user_code")
  val userCode: String,

  @SerialName("verification_url")
  val verificationURL: String,

  @SerialName("expires_in")
  val expiresIn: Long,

  val interval: Long,
)
