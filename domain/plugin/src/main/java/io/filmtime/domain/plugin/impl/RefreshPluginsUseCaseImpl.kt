package io.filmtime.domain.plugin.impl

import io.filmtime.data.plugin.discovery.PluginDiscoveryRepository
import io.filmtime.domain.plugin.RefreshPluginsUseCase
import javax.inject.Inject

internal class RefreshPluginsUseCaseImpl @Inject constructor(
  private val pluginDiscoveryRepository: PluginDiscoveryRepository,
) : RefreshPluginsUseCase {

  override suspend fun invoke() {
    pluginDiscoveryRepository.refreshPlugins()
  }
}
