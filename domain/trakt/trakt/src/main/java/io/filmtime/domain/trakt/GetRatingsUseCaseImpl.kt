package io.filmtime.domain.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoType
import io.filmtime.data.trakt.TraktRepository
import javax.inject.Inject

internal class GetRatingsUseCaseImpl @Inject constructor(
  private val traktRepository: TraktRepository,
) : GetRatingsUseCase {

  override suspend fun invoke(type: VideoType, tmdbId: Int): Result<Ratings, GeneralError> =
    traktRepository.ratings(type, tmdbId)
}
