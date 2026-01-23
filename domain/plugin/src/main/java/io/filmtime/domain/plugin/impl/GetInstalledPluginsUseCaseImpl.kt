package io.filmtime.domain.plugin.impl

import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.data.plugin.discovery.PluginDiscoveryRepository
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetInstalledPluginsUseCaseImpl @Inject constructor(
  private val pluginDiscoveryRepository: PluginDiscoveryRepository,
) : GetInstalledPluginsUseCase {

  override fun invoke(): Flow<List<PluginMetadata>> =
    pluginDiscoveryRepository.getInstalledPlugins()
}
