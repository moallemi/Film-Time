package io.filmtime.data.api.trakt.model

import io.filmtime.data.model.TraktTokens
import io.filmtime.data.network.trakt.TraktAccessTokenResponse

internal fun TraktAccessTokenResponse.toAccessToken() =
  TraktTokens(
    accessToken = accessToken,
    refreshToken = refreshToken,
  )
