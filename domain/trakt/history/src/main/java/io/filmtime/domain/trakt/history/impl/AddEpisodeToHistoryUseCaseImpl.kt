package io.filmtime.domain.trakt.history.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.trakt.TraktHistoryRepository
import io.filmtime.domain.trakt.history.AddEpisodeToHistoryUseCase
import javax.inject.Inject

internal class AddEpisodeToHistoryUseCaseImpl @Inject constructor(
  private val traktHistoryRepository: TraktHistoryRepository,
) : AddEpisodeToHistoryUseCase {

  override suspend fun invoke(
    tmdbId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
  ): Result<Unit, GeneralError> =
    traktHistoryRepository.addEpisodeToHistory(tmdbId, seasonNumber, episodeNumber)
}
