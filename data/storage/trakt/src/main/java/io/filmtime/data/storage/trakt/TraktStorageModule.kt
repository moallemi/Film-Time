package io.filmtime.data.storage.trakt

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val TRAKT_AUTH_PREFERENCES = "trakt_auth_preferences"

@InstallIn(SingletonComponent::class)
@Module
internal object TraktStorageModule {

  @Singleton
  @Provides
  fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() },
      ),
      migrations = listOf(SharedPreferencesMigration(context, TRAKT_AUTH_PREFERENCES)),
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
      produceFile = { context.preferencesDataStoreFile(TRAKT_AUTH_PREFERENCES) },
    )
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TraktStorageAbstractModule {
  @Binds
  abstract fun providesTraktAuthLocalSource(
    impl: TraktAuthLocalSourceImpl,
  ): TraktAuthLocalSource
}
