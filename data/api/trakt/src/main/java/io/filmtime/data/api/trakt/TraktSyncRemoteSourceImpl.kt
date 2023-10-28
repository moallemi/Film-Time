package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.network.adapter.NetworkResponse
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
  
  override suspend fun getHistoryById(id: String): Result<Boolean, GeneralError> {

    // TODO: move check token in a function
    val tokens = traktAuthLocalSource.tokens.firstOrNull() ?: return Result.Failure(GeneralError.ApiError("Unauthorized", 401))
    val result = traktSyncService.getHistoryById(
      type = "movies",
      id = id,
      accessToken = "Bearer " + tokens.accessToken,
    )
    return when (result) {
      is NetworkResponse.ApiError -> TODO()
      is NetworkResponse.NetworkError -> TODO()
      is NetworkResponse.Success -> {
        val watched = result.body?.any { it.movie.ids.trakt == id.toLong() } ?: false
        Result.Success(watched)
      }
      is NetworkResponse.UnknownError -> TODO()
    }
  }
}
