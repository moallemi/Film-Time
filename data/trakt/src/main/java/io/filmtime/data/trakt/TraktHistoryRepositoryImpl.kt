package io.filmtime.data.trakt

import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.api.trakt.TraktSyncRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktHistory
import javax.inject.Inject

class TraktHistoryRepositoryImpl @Inject constructor(
  private val traktSyncRemoteSource: TraktSyncRemoteSource,
  private val traktSearchRemoteSource: TraktSearchRemoteSource,
) : TraktHistoryRepository {

  override suspend fun isWatched(tmdbId: Int): Result<TraktHistory, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getByTmdbId(tmdbId)) {
      is Result.Failure -> traktIdResult
      is Result.Success -> {
        val traktId = traktIdResult.data
        traktSyncRemoteSource.getHistoryById(traktId)
          .mapSuccess { isWatched ->
            TraktHistory(traktId, isWatched)
          }
      }
    }

  override suspend fun addToHistory(traktId: Int): Result<Unit, GeneralError> {
    return traktSyncRemoteSource.addToHistory(traktId)
  }
}
