package io.filmtime.feature.plugin.manager

import io.filmtime.core.plugin.api.PluginMetadata

internal sealed interface PluginManagerAction {
  data object Refresh : PluginManagerAction
  data class SetDefaultPlugin(val pluginId: String?) : PluginManagerAction
  data class LoginPlugin(val plugin: PluginMetadata) : PluginManagerAction
  data class LoginResult(val success: Boolean) : PluginManagerAction
  data class LogoutPlugin(val plugin: PluginMetadata) : PluginManagerAction
}
