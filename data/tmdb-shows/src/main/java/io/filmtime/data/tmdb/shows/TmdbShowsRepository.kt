package io.filmtime.data.tmdb.shows

import androidx.paging.PagingData
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface TmdbShowsRepository {

  suspend fun showDetails(showId: Int): Result<VideoDetail, GeneralError>

  suspend fun trendingShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun popularShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun topRatedShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun onTheAirShows(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun airingTodayShows(): Result<List<VideoThumbnail>, GeneralError>

  fun showsStream(type: ShowListType): Flow<PagingData<VideoThumbnail>>
}

enum class ShowListType {
  Trending,
  Popular,
  TopRated,
  OnTheAir,
  AiringToday,
}
