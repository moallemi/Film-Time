package io.filmtime.data.api.tmdb

import io.filmtime.data.model.CreditItem
import io.filmtime.data.network.Cast

private val TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original//7UIm9RoBnlqS1uLlbElAY8urdWD.jpg"

fun Cast.toCreditItem() =
  CreditItem(
    name = name.orEmpty(),
    profile = TMDB_BASE_IMAGE_URL.plus(profilePath),
  )
