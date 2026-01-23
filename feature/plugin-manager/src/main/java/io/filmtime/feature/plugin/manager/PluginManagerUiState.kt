package io.filmtime.feature.plugin.manager

import io.filmtime.core.plugin.api.PluginMetadata

data class PluginManagerUiState(
  val isLoading: Boolean = true,
  val plugins: List<PluginMetadata> = emptyList(),
  val defaultPluginId: String? = null,
)
