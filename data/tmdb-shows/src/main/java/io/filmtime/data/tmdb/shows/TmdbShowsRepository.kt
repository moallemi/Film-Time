package io.filmtime.data.tmdb.shows

import androidx.paging.PagingData
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface TmdbShowsRepository {

  suspend fun getShowDetails(showId: Int): Result<VideoDetail, GeneralError>

  suspend fun getTrendingShows(): Result<List<VideoThumbnail>, GeneralError>

  fun getAllTrendingShows(): Flow<PagingData<VideoThumbnail>>
}
