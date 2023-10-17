package io.filmtime.data.tmdb.shows

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.filmtime.data.api.tmdb.TmdbShowsRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow
import paging.ShowsPagingSource
import javax.inject.Inject

internal class TmdbShowsRepositoryImpl @Inject constructor(
  private val tmdbShowsRemoteSource: TmdbShowsRemoteSource,
) : TmdbShowsRepository {

  override suspend fun getShowDetails(showId: Int): Result<VideoDetail, GeneralError> =
    tmdbShowsRemoteSource.getShowDetails(showId)

  override suspend fun getTrendingShows(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.getTrendingShows(1)

  override fun getAllTrendingShows(): Flow<PagingData<VideoThumbnail>> {
    return Pager(
      config = PagingConfig(
        pageSize = 10,
        prefetchDistance = 20,
        enablePlaceholders = false,
        initialLoadSize = 10
      ),
      pagingSourceFactory = {
        ShowsPagingSource(tmdbShowsRemoteSource)
      }
    ).flow
  }


}
