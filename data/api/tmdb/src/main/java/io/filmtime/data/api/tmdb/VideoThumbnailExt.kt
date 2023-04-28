package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbMovieDetailsResponse

fun TmdbMovieDetailsResponse.toVideoThumbnail() =
  VideoThumbnail(
    ids = VideoId(
      traktId = null,
      tmdbId = id,
    ),
    title = title ?: "",
    posterUrl = posterPath ?: "",
    year = releaseDate?.take(4)?.toInt() ?: 0,
  )
