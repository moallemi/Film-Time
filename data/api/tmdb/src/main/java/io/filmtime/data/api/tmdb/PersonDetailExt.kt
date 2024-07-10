package io.filmtime.data.api.tmdb

import io.filmtime.data.model.PersonDetail
import io.filmtime.data.network.trakt.TraktPersonResponse

internal fun TraktPersonResponse.toPerson() =
  PersonDetail(
    name = name,
  )
