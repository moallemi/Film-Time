package io.filmtime.data.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktHistory
import io.filmtime.data.model.VideoType

interface TraktHistoryRepository {

  suspend fun isWatched(tmdbId: Int, type: VideoType): Result<TraktHistory, GeneralError>

  suspend fun addToHistory(traktId: Int): Result<Unit, GeneralError>

  suspend fun removeFromHistory(traktId: Int): Result<Unit, GeneralError>
}
