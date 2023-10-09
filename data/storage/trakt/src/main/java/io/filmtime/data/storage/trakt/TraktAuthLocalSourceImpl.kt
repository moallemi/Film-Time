package io.filmtime.data.storage.trakt

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.filmtime.data.model.TraktTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TraktAuthLocalSourceImpl @Inject constructor(
  private val dataStore: DataStore<Preferences>,
) : TraktAuthLocalSource {

  private val accessTokenKey = stringPreferencesKey("access_token")
  private val refreshTokenKey = stringPreferencesKey("refresh_token")

  override val tokens: Flow<TraktTokens?>
    get() = dataStore.data.map { preferences ->
      val accessToken = preferences[accessTokenKey]
      val refreshToken = preferences[refreshTokenKey]
      if (accessToken == null || refreshToken == null) return@map null
      TraktTokens(accessToken, refreshToken)
    }

  override suspend fun storeAuthTokens(token: TraktTokens) {
    dataStore.edit { preferences ->
      preferences[accessTokenKey] = token.accessToken
      preferences[refreshTokenKey] = token.refreshToken
    }
  }
}
