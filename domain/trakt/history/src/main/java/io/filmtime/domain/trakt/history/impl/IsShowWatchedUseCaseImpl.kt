package io.filmtime.domain.trakt.history.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktEpisodeHistory
import io.filmtime.data.trakt.TraktHistoryRepository
import io.filmtime.domain.trakt.history.IsShowWatchedUseCase
import javax.inject.Inject

internal class IsShowWatchedUseCaseImpl @Inject constructor(
  private val traktHistoryRepository: TraktHistoryRepository,
) : IsShowWatchedUseCase {

  override suspend fun invoke(
    tmdbId: Int,
    seasonNumber: Int,
  ): Result<Map<Int, List<TraktEpisodeHistory>>, GeneralError> =
    traktHistoryRepository.isShowWatched(tmdbId = tmdbId, seasonNumber = seasonNumber)
}
