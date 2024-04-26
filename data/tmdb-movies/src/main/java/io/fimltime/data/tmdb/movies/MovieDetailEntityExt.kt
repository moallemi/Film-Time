package io.fimltime.data.tmdb.movies

import io.filmtime.data.database.MovieDetailEntity
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoId

fun MovieDetailEntity.toMovie(): VideoDetail {
  return VideoDetail(
    ids = VideoId(tmdbId = tmdbId, traktId = traktId),
    title = name,
    posterUrl = posterUrl,
    coverUrl = coverUrl,
    year = year,
    genres = listOf(),
    description = description,
    releaseDate = releaseDate,
    isWatched = isWatched,
    runtime = runtime,
    voteAverage = voteAverage,
    voteColor = voteColor,
    originalLanguage = originalLanguage,
    spokenLanguages = listOf(),
  )
}

fun VideoDetail.toEntity(): MovieDetailEntity {
  return MovieDetailEntity(
    traktId = ids.traktId,
    tmdbId = ids.tmdbId!!,
    name = title,
    coverUrl = coverUrl,
    description = description,
//    genres = genres.filterNotNull(),
    isWatched = isWatched ?: false,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    year = year,
//    spokenLanguages = spokenLanguages,
    originalLanguage = originalLanguage,
    voteColor = voteColor,
    voteAverage = voteAverage,
    runtime = runtime,
  )
}
