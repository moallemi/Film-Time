package io.filmtime.data.plugin.discovery

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PluginDiscoveryModule {

  @Binds
  @Singleton
  internal abstract fun bindPluginDiscoveryRepository(
    impl: PluginDiscoveryRepositoryImpl,
  ): PluginDiscoveryRepository
}
