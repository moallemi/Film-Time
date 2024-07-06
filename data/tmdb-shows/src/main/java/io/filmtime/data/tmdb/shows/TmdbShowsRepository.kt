package io.filmtime.data.tmdb.shows

import androidx.paging.PagingData
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
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

  suspend fun credits(movieId: Int): Result<List<Person>, GeneralError>

  suspend fun similar(movieId: Int): Result<List<VideoThumbnail>, GeneralError>

  suspend fun episodesBySeason(showId: Int, seasonNumber: Int): Result<List<EpisodeThumbnail>, GeneralError>
}

enum class ShowListType {
  Trending,
  Popular,
  TopRated,
  OnTheAir,
  AiringToday,
}
