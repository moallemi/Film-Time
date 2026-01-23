package io.filmtime.feature.plugin.manager

interface PluginPreferences {
  fun getDefaultPluginId(): String?
  fun setDefaultPluginId(pluginId: String?)
}
