package io.filmtime.data.api.trakt

import io.filmtime.data.model.TraktAccessToken
import io.filmtime.data.network.TraktAccessTokenResponse


fun TraktAccessTokenResponse.toAccessToken() =
    TraktAccessToken(
      accessToken = accessToken,
      refreshToken = refreshToken,
    )
