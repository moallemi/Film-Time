package io.filmtime.domain.tmdb.shows

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.tmdb.shows.ShowListType
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveShowsStreamUseCaseImpl @Inject constructor(
  private val tmdbShowsRepository: TmdbShowsRepository,
) : ObserveShowsStreamUseCase {

  override fun invoke(showListType: ShowListType): Flow<PagingData<VideoThumbnail>> =
    tmdbShowsRepository.showsStream(showListType)
}
