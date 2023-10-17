package io.filmtime.domain.tmdb.shows

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface GetTrendingShowsPagingUsecase  {
  operator fun invoke (): Flow<PagingData<VideoThumbnail>>
}
