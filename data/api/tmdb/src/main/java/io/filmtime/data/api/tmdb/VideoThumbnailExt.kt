package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType
import io.filmtime.data.network.TmdbMovieDetailsResponse
import io.filmtime.data.network.TmdbShowDetailsResponse
import io.filmtime.data.network.TmdbShowResultResponse
import io.filmtime.data.network.TmdbVideoResultResponse
import java.util.concurrent.TimeUnit

private val TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/"

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
    runtime = fromMinutesToHHmm(runtime ?: 0),
    releaseDate = releaseDate?.split("-")?.get(0) ?: "N/A",
  )

fun TmdbShowDetailsResponse.toVideoDetail() =
  VideoDetail(
    ids = VideoId(
      traktId = null,
      tmdbId = id,
    ),
    title = name ?: "",
    posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
    coverUrl = TMDB_BASE_IMAGE_URL.plus(backdropPath),
    year = firstAirDate?.take(4)?.toInt() ?: 0,
    genres = genres?.map { it.name } ?: listOf<String>(),
    originalLanguage = originalLanguage,
    spokenLanguages = spokenLanguages?.map { it.englishName ?: "" }?.filter { it.isNotEmpty() }
      ?: listOf(),
    description = overview ?: "",
    runtime = "",
    releaseDate = "",
  )

fun TmdbVideoResultResponse.toVideoThumbnail() = VideoThumbnail(
  ids = VideoId(traktId = null, tmdbId = id),
  title = title.orEmpty(),
  posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
  year = if (releaseDate?.isNotEmpty() == true) {
    releaseDate!!.take(4).toInt()
  } else {
    0
  },
  type = VideoType.Movie,
)

fun TmdbShowResultResponse.toVideoThumbnail() = VideoThumbnail(
  ids = VideoId(traktId = null, tmdbId = id),
  title = name.orEmpty(),
  posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
  year = releaseDate?.take(4)?.toInt() ?: 0,
  type = VideoType.Show,
)

fun fromMinutesToHHmm(minutes: Long): String {
  val hours = TimeUnit.MINUTES.toHours(minutes)
  val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
  return String.format("%02dh %02dm", hours, remainMinutes)
}
