package io.filmtime.data.api.trakt.model

import io.filmtime.data.model.TraktCodeLogin
import io.filmtime.data.network.trakt.TraktDeviceCodeResponse

fun TraktDeviceCodeResponse.toDeviceCode() =
  TraktCodeLogin(
    deviceCode = deviceCode,
    userCode = userCode,
    verificationURL = verificationURL,
    expiresIn = expiresIn,
    interval = interval,
  )
