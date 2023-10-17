package io.filmtime.domain.tmdb.shows

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrendingShowsPagingUsecaseImpl @Inject constructor(
  private val tmdbShowsRepository: TmdbShowsRepository
):GetTrendingShowsPagingUsecase {
  override fun invoke(): Flow<PagingData<VideoThumbnail>> {
    return  tmdbShowsRepository.getAllTrendingShows()
  }
}
