package io.filmtime.domain.trakt.history.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.trakt.TraktHistoryRepository
import io.filmtime.domain.trakt.history.RemoveFromHistoryUseCase
import javax.inject.Inject

internal class RemoveFromHistoryUseCaseImpl @Inject constructor(
  private val traktHistoryRepository: TraktHistoryRepository,
) : RemoveFromHistoryUseCase {

  override suspend fun invoke(id: Int): Result<Unit, GeneralError> =
    traktHistoryRepository.removeFromHistory(id)
}
