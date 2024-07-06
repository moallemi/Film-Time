package io.filmtime.data.api.tmdb

import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbShowsRemoteSource {

  suspend fun showDetails(showId: Int): Result<VideoDetail, GeneralError>

  suspend fun trendingShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun popularShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun topRatedShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun onTheAirShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun airingTodayShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun credits(showId: Int): Result<List<Person>, GeneralError>

  suspend fun similar(showId: Int): Result<List<VideoThumbnail>, GeneralError>

  suspend fun episodesBySeason(showId: Int, seasonNumber: Int): Result<List<EpisodeThumbnail>, GeneralError>

  companion object {
    const val PAGE_SIZE = 20 // TMDB API default page size
  }
}
