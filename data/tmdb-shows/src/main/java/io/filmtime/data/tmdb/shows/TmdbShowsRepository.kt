package io.filmtime.data.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbShowsRepository {

  suspend fun showDetails(showId: Int): Result<VideoDetail, GeneralError>

  suspend fun trendingShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun popularShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun topRatedShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun onTheAirShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun airingTodayShows(): Result<List<VideoThumbnail>, GeneralError>
}
