package io.filmtime.data.trakt

import io.filmtime.data.api.trakt.TraktSyncRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import javax.inject.Inject

class TraktHistoryRepositoryImpl @Inject constructor(
  private val traktSyncRemoteSource: TraktSyncRemoteSource,
) : TraktHistoryRepository {
  override suspend fun addToHistory(id: String): Result<Unit, GeneralError> {
    return traktSyncRemoteSource.addToHistory(id)
  }
}
