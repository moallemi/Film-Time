package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktEpisodeHistory

interface TraktSyncRemoteSource {

  suspend fun getAllHistories(): Result<Nothing, GeneralError>

  suspend fun getMovieHistory(id: Int): Result<Boolean, GeneralError>

  suspend fun getShowHistory(id: Int): Result<Map<Int, List<TraktEpisodeHistory>>, GeneralError>

  suspend fun addToHistory(id: Int): Result<Unit, GeneralError>

  suspend fun removeMovieFromHistory(id: Int): Result<Unit, GeneralError>

  suspend fun addEpisodeToHistory(id: Int, seasonNumber: Int, episodeNumber: Int): Result<Unit, GeneralError>

  suspend fun removeEpisodeFromHistory(id: Int, seasonNumber: Int, episodeNumber: Int): Result<Unit, GeneralError>
}
