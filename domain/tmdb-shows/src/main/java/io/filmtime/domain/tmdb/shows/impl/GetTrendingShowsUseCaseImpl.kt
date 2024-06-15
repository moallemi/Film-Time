package io.filmtime.domain.tmdb.shows.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.filmtime.domain.tmdb.shows.GetTrendingShowsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetTrendingShowsUseCaseImpl @Inject constructor(
  private val tmdbShowsRepository: TmdbShowsRepository,
) : GetTrendingShowsUseCase {

  override suspend fun invoke(): Flow<Result<List<VideoThumbnail>, GeneralError>> = flow {
    emit(tmdbShowsRepository.trendingShows())
  }
}
