package io.filmtime.data.trakt

import io.filmtime.data.api.trakt.TraktMediaType.Movie
import io.filmtime.data.api.trakt.TraktMediaType.Show
import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.api.trakt.TraktSyncRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktEpisodeHistory
import io.filmtime.data.model.TraktMovieHistory
import javax.inject.Inject

internal class TraktHistoryRepositoryImpl @Inject constructor(
  private val traktSyncRemoteSource: TraktSyncRemoteSource,
  private val traktSearchRemoteSource: TraktSearchRemoteSource,
) : TraktHistoryRepository {

  override suspend fun isMovieWatched(tmdbId: Int): Result<TraktMovieHistory, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getByTmdbId(tmdbId, Movie)) {
      is Result.Success -> {
        val traktId = traktIdResult.data
        traktSyncRemoteSource.getMovieHistory(traktId)
          .mapSuccess { isWatched ->
            TraktMovieHistory(traktId, isWatched)
          }
      }

      is Result.Failure -> traktIdResult
    }

  override suspend fun isShowWatched(
    tmdbId: Int,
    seasonNumber: Int,
  ): Result<Map<Int, List<TraktEpisodeHistory>>, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getByTmdbId(tmdbId, Show)) {
      is Result.Success -> traktSyncRemoteSource.getShowHistory(traktIdResult.data)
      is Result.Failure -> traktIdResult
    }

  override suspend fun addMovieToHistory(traktId: Int): Result<Unit, GeneralError> =
    traktSyncRemoteSource.addToHistory(traktId)

  override suspend fun removeMovieFromHistory(traktId: Int): Result<Unit, GeneralError> =
    traktSyncRemoteSource.removeMovieFromHistory(traktId)

  override suspend fun addEpisodeToHistory(
    tmdbId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
  ): Result<Unit, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getByTmdbId(tmdbId, Show)) {
      is Result.Success -> traktSyncRemoteSource.addEpisodeToHistory(traktIdResult.data, seasonNumber, episodeNumber)
      is Result.Failure -> traktIdResult
    }

  override suspend fun removeEpisodeFromHistory(
    tmdbId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
  ): Result<Unit, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getByTmdbId(tmdbId, Show)) {
      is Result.Success -> traktSyncRemoteSource.removeEpisodeFromHistory(
        traktIdResult.data,
        seasonNumber,
        episodeNumber,
      )
      is Result.Failure -> traktIdResult
    }
}
