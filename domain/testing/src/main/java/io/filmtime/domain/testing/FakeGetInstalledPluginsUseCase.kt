package io.filmtime.domain.testing

import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGetInstalledPluginsUseCase : GetInstalledPluginsUseCase {

  private val pluginsFlow = MutableStateFlow<List<PluginMetadata>>(emptyList())

  fun setPlugins(plugins: List<PluginMetadata>) {
    pluginsFlow.value = plugins
  }

  override fun invoke(): Flow<List<PluginMetadata>> = pluginsFlow
}
