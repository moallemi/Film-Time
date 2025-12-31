package io.filmtime.data.model

data class TraktCodeLogin(
  val deviceCode: String,
  val userCode: String,
  val verificationURL: String,
  val expiresIn: Long,
  val interval: Long,
)
