package io.filmtime.core.plugin.api

sealed class PluginError {
  data object NoPluginsInstalled : PluginError()
  data object PluginNotFound : PluginError()
  data object NoStreamsAvailable : PluginError()
  data class PluginException(val message: String) : PluginError()
  data class CommunicationError(val message: String) : PluginError()
}
