package io.filmtime.feature.plugin.manager.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.feature.plugin.manager.PluginPreferences
import io.filmtime.feature.plugin.manager.PluginPreferencesImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PluginPreferencesModule {

  @Binds
  @Singleton
  abstract fun bindPluginPreferences(
    impl: PluginPreferencesImpl,
  ): PluginPreferences
}
