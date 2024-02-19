package io.filmtime.domain.tmdb.shows

import androidx.paging.PagingData
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.filmtime.domain.tmdb.shows.model.toShowListType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveShowsStreamUseCaseImpl @Inject constructor(
  private val tmdbShowsRepository: TmdbShowsRepository,
) : ObserveShowsStreamUseCase {

  override fun invoke(videoListType: VideoListType): Flow<PagingData<VideoThumbnail>> =
    tmdbShowsRepository.showsStream(videoListType.toShowListType())
}
