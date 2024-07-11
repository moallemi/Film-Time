package io.filmtime.data.api.tmdb

import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.data.network.model.CollectionResponse
import io.filmtime.data.network.model.Part

fun CollectionResponse.toCollection() =
  MovieCollection(
    id = id,
    name = name,
    parts = parts.map { it.toVideo() },
    overview = overview,
    backdropPath = TMDB_BASE_IMAGE_URL.plus(backdropPath),
    posterPath = TMDB_BASE_IMAGE_URL.plus(posterPath),
  )

fun Part.toVideo() =
  VideoThumbnail(
    title = title,
    type = Movie,
    ids = VideoId(tmdbId = id.toInt(), traktId = null),
    posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
    year = releaseDate.takeIf { it.isNotEmpty() }?.take(4)?.toInt() ?: 0,
  )
