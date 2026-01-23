package io.filmtime.domain.plugin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
import io.filmtime.domain.plugin.GetStreamFromPluginUseCase
import io.filmtime.domain.plugin.RefreshPluginsUseCase
import io.filmtime.domain.plugin.impl.GetInstalledPluginsUseCaseImpl
import io.filmtime.domain.plugin.impl.GetStreamFromPluginUseCaseImpl
import io.filmtime.domain.plugin.impl.RefreshPluginsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PluginModule {

  @Binds
  abstract fun bindGetInstalledPluginsUseCase(
    impl: GetInstalledPluginsUseCaseImpl,
  ): GetInstalledPluginsUseCase

  @Binds
  abstract fun bindRefreshPluginsUseCase(
    impl: RefreshPluginsUseCaseImpl,
  ): RefreshPluginsUseCase

  @Binds
  abstract fun bindGetStreamFromPluginUseCase(
    impl: GetStreamFromPluginUseCaseImpl,
  ): GetStreamFromPluginUseCase
}
