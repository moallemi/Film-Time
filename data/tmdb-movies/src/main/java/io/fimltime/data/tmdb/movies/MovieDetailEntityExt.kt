package io.fimltime.data.tmdb.movies

import io.filmtime.data.database.entity.MovieDetailEntity
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoId

fun MovieDetailEntity.toMovie(): VideoDetail {
  return VideoDetail(
    ids = VideoId(tmdbId = tmdbId, traktId = null),
    title = name,
    posterUrl = posterUrl,
    coverUrl = coverUrl,
    year = year,
    genres = genres,
    description = description,
    releaseDate = releaseDate,
    isWatched = null,
    runtime = runtime,
    originalLanguage = originalLanguage,
    spokenLanguages = spokenLanguages,
    budget = budget,
    homePage = homePage,
    status = status,
    tagline = tagline,
    networks = null,
    seasonsNumber = null,
    collectionId = null,
  )
}

fun VideoDetail.toEntity(): MovieDetailEntity {
  return MovieDetailEntity(
    tmdbId = ids.tmdbId!!,
    name = title,
    coverUrl = coverUrl,
    description = description,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    year = year,
    originalLanguage = originalLanguage,
    runtime = runtime,
    genres = genres,
    spokenLanguages = spokenLanguages,
    homePage = homePage,
    status = status,
    budget = budget,
    tagline = tagline,
  )
}
