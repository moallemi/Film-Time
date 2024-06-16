package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface TraktSyncRemoteSource {

  suspend fun getAllHistories(): Result<Nothing, GeneralError>

  suspend fun getHistoryById(id: Int): Result<Boolean, GeneralError>

  suspend fun addToHistory(id: Int): Result<Unit, GeneralError>

  suspend fun removeFromHistory(id: Int): Result<Unit, GeneralError>
}
