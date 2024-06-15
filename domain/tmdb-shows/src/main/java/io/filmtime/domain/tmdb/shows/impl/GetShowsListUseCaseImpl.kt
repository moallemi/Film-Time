package io.filmtime.domain.tmdb.shows.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoListType.AiringToday
import io.filmtime.data.model.VideoListType.NowPlaying
import io.filmtime.data.model.VideoListType.OnTheAir
import io.filmtime.data.model.VideoListType.Popular
import io.filmtime.data.model.VideoListType.TopRated
import io.filmtime.data.model.VideoListType.Trending
import io.filmtime.data.model.VideoListType.Upcoming
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.filmtime.domain.tmdb.shows.GetShowsListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class GetShowsListUseCaseImpl @Inject constructor(
  private val showsRepository: TmdbShowsRepository,
) : GetShowsListUseCase {

  override suspend fun invoke(videoListType: VideoListType): Flow<Result<List<VideoThumbnail>, GeneralError>> =
    when (videoListType) {
      Trending -> showsRepository.trendingShows()
      Popular -> showsRepository.popularShows()
      TopRated -> showsRepository.topRatedShows()
      NowPlaying -> throw UnsupportedOperationException("NowPlaying is not supported for shows")
      Upcoming -> throw UnsupportedOperationException("Upcoming is not supported for shows")
      OnTheAir -> showsRepository.onTheAirShows()
      AiringToday -> showsRepository.airingTodayShows()
    }.let(::flowOf)
}
