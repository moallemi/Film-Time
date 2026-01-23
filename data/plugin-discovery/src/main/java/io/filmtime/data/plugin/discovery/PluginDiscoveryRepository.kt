package io.filmtime.data.plugin.discovery

import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.plugin.api.StreamRequest
import io.filmtime.core.plugin.api.StreamResponse
import io.filmtime.data.model.Result
import kotlinx.coroutines.flow.Flow

interface PluginDiscoveryRepository {

  fun getInstalledPlugins(): Flow<List<PluginMetadata>>

  suspend fun refreshPlugins()

  suspend fun getStreamFromPlugin(
    pluginId: String,
    request: StreamRequest,
  ): Result<StreamResponse, PluginError>

  suspend fun getPluginAuthState(pluginId: String): Result<PluginAuthState, PluginError>

  suspend fun logoutPlugin(pluginId: String): Result<Boolean, PluginError>
}
