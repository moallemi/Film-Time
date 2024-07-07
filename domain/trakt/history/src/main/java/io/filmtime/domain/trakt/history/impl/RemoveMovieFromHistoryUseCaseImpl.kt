package io.filmtime.domain.trakt.history.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.trakt.TraktHistoryRepository
import io.filmtime.domain.trakt.history.RemoveMovieFromHistoryUseCase
import javax.inject.Inject

internal class RemoveMovieFromHistoryUseCaseImpl @Inject constructor(
  private val traktHistoryRepository: TraktHistoryRepository,
) : RemoveMovieFromHistoryUseCase {

  override suspend fun invoke(id: Int): Result<Unit, GeneralError> =
    traktHistoryRepository.removeMovieFromHistory(id)
}
