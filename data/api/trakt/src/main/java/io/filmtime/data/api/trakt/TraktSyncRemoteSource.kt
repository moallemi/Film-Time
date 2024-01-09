package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface TraktSyncRemoteSource {

  suspend fun getAllHistories(): Result<Nothing, GeneralError>

  suspend fun getHistoryById(id: String): Result<Boolean, GeneralError>

  suspend fun addToHistory(id: String): Result<Unit, GeneralError>
}
