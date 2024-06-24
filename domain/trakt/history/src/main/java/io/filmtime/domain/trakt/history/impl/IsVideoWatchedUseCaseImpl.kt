package io.filmtime.domain.trakt.history.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktHistory
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.data.trakt.TraktHistoryRepository
import io.filmtime.domain.trakt.history.IsVideoWatchedUseCase
import javax.inject.Inject

internal class IsVideoWatchedUseCaseImpl @Inject constructor(
  private val traktHistoryRepository: TraktHistoryRepository,
) : IsVideoWatchedUseCase {

  override suspend fun invoke(tmdbId: Int): Result<TraktHistory, GeneralError> =
    traktHistoryRepository.isWatched(tmdbId = tmdbId, type = Movie)
}
