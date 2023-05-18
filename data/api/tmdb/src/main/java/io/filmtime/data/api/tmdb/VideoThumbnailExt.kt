package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbMovieDetailsResponse
import io.filmtime.data.network.TmdbVideoResultResponse

private val TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/"

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

fun TmdbMovieDetailsResponse.toVideoDetail() =
  VideoDetail(
    ids = VideoId(
      traktId = null,
      tmdbId = id,
    ),
    title = title ?: "",
    posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
    coverUrl = TMDB_BASE_IMAGE_URL.plus(backdropPath),
    year = releaseDate?.take(4)?.toInt() ?: 0,
    genres = genres?.map { it.name } ?: listOf<String>(),
    originalLanguage = originalLanguage,
    spokenLanguages = spokenLanguages?.map { it.englishName ?: "" }?.filter { it.isNotEmpty() }
      ?: listOf(),
    description = overview ?: "",
  )

fun TmdbVideoResultResponse.toVideoThumbnail() = VideoThumbnail(
  ids = VideoId(traktId = id, tmdbId = null),
  title = title.orEmpty(),
  posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
  year = releaseDate?.take(4)?.toInt() ?: 0,
)
