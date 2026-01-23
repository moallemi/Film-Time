package io.filmtime.feature.plugin.manager

import android.content.Intent
import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginMetadata

data class PluginManagerUiState(
  val isLoading: Boolean = true,
  val plugins: List<PluginMetadata> = emptyList(),
  val defaultPluginId: String? = null,
  val authStates: Map<String, PluginAuthState> = emptyMap(),
  val loginIntent: Intent? = null,
  val pendingLoginPluginId: String? = null,
)
