package io.filmtime.data.trakt

import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.api.trakt.TraktSyncRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktHistory
import io.filmtime.data.model.VideoType
import io.filmtime.data.trakt.model.toTraktMediaType
import javax.inject.Inject

internal class TraktHistoryRepositoryImpl @Inject constructor(
  private val traktSyncRemoteSource: TraktSyncRemoteSource,
  private val traktSearchRemoteSource: TraktSearchRemoteSource,
) : TraktHistoryRepository {

  override suspend fun isWatched(tmdbId: Int, type: VideoType): Result<TraktHistory, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getByTmdbId(tmdbId, type.toTraktMediaType())) {
      is Result.Failure -> traktIdResult
      is Result.Success -> {
        val traktId = traktIdResult.data
        traktSyncRemoteSource.getHistoryById(traktId, type.toTraktMediaType())
          .mapSuccess { isWatched ->
            TraktHistory(traktId, isWatched)
          }
      }
    }

  override suspend fun addToHistory(traktId: Int): Result<Unit, GeneralError> =
    traktSyncRemoteSource.addToHistory(traktId)

  override suspend fun removeFromHistory(traktId: Int): Result<Unit, GeneralError> =
    traktSyncRemoteSource.removeFromHistory(traktId)
}
