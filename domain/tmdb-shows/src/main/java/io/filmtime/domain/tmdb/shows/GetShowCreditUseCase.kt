package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface GetShowCreditUseCase {
  suspend operator fun invoke(movieId: Int): Result<List<CreditItem>, GeneralError>
}
