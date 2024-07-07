package io.filmtime.domain.trakt.history.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.trakt.TraktHistoryRepository
import io.filmtime.domain.trakt.history.AddMovieToHistoryUseCase
import javax.inject.Inject

internal class AddMovieToHistoryUseCaseImpl @Inject constructor(
  private val traktHistoryRepository: TraktHistoryRepository,
) : AddMovieToHistoryUseCase {

  override suspend fun invoke(traktId: Int): Result<Unit, GeneralError> =
    traktHistoryRepository.addMovieToHistory(traktId)
}
