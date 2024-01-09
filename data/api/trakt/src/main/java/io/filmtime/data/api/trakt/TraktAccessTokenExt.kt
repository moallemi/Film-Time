package io.filmtime.data.api.trakt

import io.filmtime.data.model.TraktTokens
import io.filmtime.data.network.trakt.TraktAccessTokenResponse

fun TraktAccessTokenResponse.toAccessToken() =
  TraktTokens(
    accessToken = accessToken,
    refreshToken = refreshToken,
  )
