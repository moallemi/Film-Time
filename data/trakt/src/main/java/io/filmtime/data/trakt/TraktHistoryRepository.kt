package io.filmtime.data.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface TraktHistoryRepository {

  suspend fun addToHistory(id: String): Result<Unit, GeneralError>
}
