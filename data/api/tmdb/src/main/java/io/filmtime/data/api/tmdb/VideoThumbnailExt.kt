package io.filmtime.data.api.tmdb

import io.filmtime.data.model.SearchResult.Person
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoGenre
import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType
import io.filmtime.data.network.MovieSearchResult
import io.filmtime.data.network.PersonSearchResult
import io.filmtime.data.network.TmdbMovieDetailsResponse
import io.filmtime.data.network.TmdbShowDetailsResponse
import io.filmtime.data.network.TmdbShowResultResponse
import io.filmtime.data.network.TmdbVideoResultResponse
import io.filmtime.data.network.TvShowSearchResult

internal const val TMDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original/"

fun TmdbMovieDetailsResponse.toVideoDetail() =
  VideoDetail(
    ids = VideoId(
      traktId = null,
      tmdbId = id,
    ),
    title = title ?: "",
    posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
    coverUrl = TMDB_BASE_IMAGE_URL.plus(backdropPath),
    year = releaseDate?.takeIf { it.isNotEmpty() }?.take(4)?.toInt() ?: 0,
    genres = genres?.map { VideoGenre(it.id!!, it.name!!) } ?: listOf(),
    originalLanguage = originalLanguage,
    spokenLanguages = spokenLanguages?.map { it.englishName ?: "" }?.filter { it.isNotEmpty() }
      ?: listOf(),
    description = overview ?: "",
    runtime = runtime?.toHoursAndMinutes(),
    releaseDate = releaseDate ?: "N/A",
    homePage = homepage,
    status = status,
    budget = budget,
    tagline = tagline,
    networks = null,
    seasonsNumber = null,
    collectionId = belongsToCollection?.id,
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
    genres = genres?.map { VideoGenre(it.id!!, it.name!!) } ?: listOf(),
    originalLanguage = originalLanguage,
    spokenLanguages = spokenLanguages?.map { it.englishName ?: "" }?.filter { it.isNotEmpty() } ?: listOf(),
    description = overview ?: "",
    runtime = episodeRunTime?.firstOrNull()?.toHoursAndMinutes(),
    releaseDate = firstAirDate ?: "N/A",
    status = status,
    homePage = homepage,
    budget = null,
    tagline = tagline,
    networks = networks?.mapNotNull { it.name },
    seasonsNumber = numberOfSeasons,
    collectionId = null,
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

internal fun Long.toHoursAndMinutes(): String {
  // this is in minutes
  val hours = this / 60
  val remainingMinutes = this % 60

  return when {
    hours > 0 && remainingMinutes > 0 -> "${hours}h ${remainingMinutes}m"
    hours > 0 -> "${hours}h"
    else -> "${remainingMinutes}m"
  }
}

fun TvShowSearchResult.toVideoThumbnail() = VideoThumbnail(
  ids = VideoId(traktId = null, tmdbId = id),
  title = title.orEmpty(),
  posterUrl = TMDB_BASE_IMAGE_URL.plus(posterPath),
  year = if (releaseDate?.isNotEmpty() == true) {
    releaseDate!!.take(4).toInt()
  } else {
    0
  },
  type = VideoType.Show,
)

fun MovieSearchResult.toVideoThumbnail() = VideoThumbnail(
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

fun PersonSearchResult.toPerson(): Person = Person(
  name = name,
  imageUrl = TMDB_BASE_IMAGE_URL.plus(profilePath),
)
