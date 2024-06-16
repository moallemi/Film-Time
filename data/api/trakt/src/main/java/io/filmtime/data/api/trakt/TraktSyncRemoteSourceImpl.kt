package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.network.adapter.NetworkResponse
import io.filmtime.data.network.trakt.HistoryIDS
import io.filmtime.data.network.trakt.MovieHistory
import io.filmtime.data.network.trakt.SyncHistoryRequest
import io.filmtime.data.network.trakt.TraktSyncService
import io.filmtime.data.storage.trakt.TraktAuthLocalSource
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TraktSyncRemoteSourceImpl
@Inject constructor(
  private val traktSyncService: TraktSyncService,
  private val traktAuthLocalSource: TraktAuthLocalSource,
) : TraktSyncRemoteSource {
  override suspend fun getAllHistories(): Result<Nothing, GeneralError> {
    // TODO: move check token in a function
    traktAuthLocalSource.tokens.firstOrNull() ?: return Result.Failure(GeneralError.ApiError("Unauthorized", 401))
    val result = traktSyncService.getWatchedHistory(
      type = "movies",
      accessToken = "",
    )
    return when (result) {
      is NetworkResponse.ApiError -> TODO()
      is NetworkResponse.NetworkError -> TODO()
      is NetworkResponse.Success -> TODO()
      is NetworkResponse.UnknownError -> TODO()
    }
  }

  override suspend fun getHistoryById(id: Int): Result<Boolean, GeneralError> {
    // TODO: move check token in a function
    val tokens =
      traktAuthLocalSource.tokens.firstOrNull() ?: return Result.Failure(GeneralError.ApiError("Unauthorized", 401))
    val result = traktSyncService.getHistoryById(
      type = "movies",
      id = id,
      accessToken = "Bearer " + tokens.accessToken,
    )
    return when (result) {
      is NetworkResponse.ApiError -> Result.Failure(GeneralError.ApiError(result.body.error, result.code))
      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
      is NetworkResponse.Success -> {
        val watched = result.body?.any { it.movie.ids.trakt == id } ?: false
        Result.Success(watched)
      }
    }
  }

  override suspend fun addToHistory(id: Int): Result<Unit, GeneralError> {
    val tokens =
      traktAuthLocalSource.tokens.firstOrNull() ?: return Result.Failure(GeneralError.ApiError("Unauthorized", 401))
    val result = traktSyncService.addMovieToHistory(
      accessToken = "Bearer " + tokens.accessToken,
      body = SyncHistoryRequest(
        movies = listOf(
          MovieHistory(
            ids = HistoryIDS(
              trakt = id,
            ),
          ),
        ),
      ),
    )
    return when (result) {
      is NetworkResponse.Success -> Result.Success(Unit)
      is NetworkResponse.ApiError -> Result.Failure(GeneralError.ApiError(result.body.error, result.code))
      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }
  }

  override suspend fun removeFromHistory(id: Int): Result<Unit, GeneralError> {
    val tokens =
      traktAuthLocalSource.tokens.firstOrNull() ?: return Result.Failure(GeneralError.ApiError("Unauthorized", 401))
    val result = traktSyncService.removeMovieFromHistory(
      accessToken = "Bearer " + tokens.accessToken,
      body = SyncHistoryRequest(
        movies = listOf(
          MovieHistory(
            ids = HistoryIDS(
              trakt = id,
            ),
          ),
        ),
      ),
    )
    return when (result) {
      is NetworkResponse.Success -> Result.Success(Unit)
      is NetworkResponse.ApiError -> Result.Failure(GeneralError.ApiError(result.body.error, result.code))
      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }
  }
}
