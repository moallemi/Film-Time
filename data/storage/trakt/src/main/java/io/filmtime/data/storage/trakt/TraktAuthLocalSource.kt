package io.filmtime.data.storage.trakt

import io.filmtime.data.model.TraktTokens
import kotlinx.coroutines.flow.Flow

interface TraktAuthLocalSource {

  val tokens: Flow<TraktTokens?>
  suspend fun storeAuthTokens(token: TraktTokens)
  suspend fun clearAuthTokens()
}
