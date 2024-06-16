package io.filmtime.data.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktHistory

interface TraktHistoryRepository {

  suspend fun isWatched(tmdbId: Int): Result<TraktHistory, GeneralError>

  suspend fun addToHistory(traktId: Int): Result<Unit, GeneralError>
}
