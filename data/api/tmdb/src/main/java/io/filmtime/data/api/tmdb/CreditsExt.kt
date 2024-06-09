package io.filmtime.data.api.tmdb

import io.filmtime.data.model.Person
import io.filmtime.data.model.PersonType
import io.filmtime.data.network.PersonDto

fun PersonDto.asCastItem() =
  Person(
    id = id,
    name = name.orEmpty(),
    imageUrl = TMDB_BASE_IMAGE_URL.plus(profilePath),
    role = character.orEmpty(),
    type = PersonType.Cast,
  )

fun PersonDto.asCrewItem() =
  Person(
    id = id,
    name = name.orEmpty(),
    imageUrl = TMDB_BASE_IMAGE_URL.plus(profilePath),
    role = job.orEmpty(),
    type = PersonType.Crew,
  )
