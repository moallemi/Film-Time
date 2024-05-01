package io.fimltime.data.tmdb.movies

import io.filmtime.data.database.MovieDetailEntity
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoId

fun MovieDetailEntity.toMovie(): VideoDetail {
  return VideoDetail(
    ids = VideoId(tmdbId = tmdbId, traktId = null),
    title = name,
    posterUrl = posterUrl,
    coverUrl = coverUrl,
    year = year,
    genres = listOf(),
    description = description,
    releaseDate = releaseDate,
    isWatched = null,
    runtime = runtime,
    voteAverage = 0F,
    voteColor = 0,
    originalLanguage = originalLanguage,
    spokenLanguages = listOf(),
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
  )
}
