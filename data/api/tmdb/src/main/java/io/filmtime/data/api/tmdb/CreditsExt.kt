package io.filmtime.data.api.tmdb

import io.filmtime.data.model.CreditItem
import io.filmtime.data.network.Cast

fun Cast.toCreditItem() =
  CreditItem(
    id = id,
    name = name.orEmpty(),
    profile = TMDB_BASE_IMAGE_URL.plus(profilePath),
  )
