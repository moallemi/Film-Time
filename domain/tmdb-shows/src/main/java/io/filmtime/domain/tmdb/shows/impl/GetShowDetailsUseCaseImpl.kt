package io.filmtime.domain.tmdb.shows.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.filmtime.domain.tmdb.shows.GetShowDetailsUseCase
import javax.inject.Inject

internal class GetShowDetailsUseCaseImpl @Inject constructor(
  private val tmdbShowsRepository: TmdbShowsRepository,
) : GetShowDetailsUseCase {
  override suspend fun invoke(showId: Int): Result<VideoDetail, GeneralError> =
    tmdbShowsRepository.showDetails(showId)
}
