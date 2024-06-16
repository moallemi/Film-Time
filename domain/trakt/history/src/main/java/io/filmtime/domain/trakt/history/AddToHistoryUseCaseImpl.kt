package io.filmtime.domain.trakt.history

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.trakt.TraktHistoryRepository
import javax.inject.Inject

internal class AddToHistoryUseCaseImpl @Inject constructor(
  private val traktHistoryRepository: TraktHistoryRepository,
) : AddToHistoryUseCase {

  override suspend fun invoke(traktId: Int): Result<Unit, GeneralError> =
    traktHistoryRepository.addToHistory(traktId)
}
