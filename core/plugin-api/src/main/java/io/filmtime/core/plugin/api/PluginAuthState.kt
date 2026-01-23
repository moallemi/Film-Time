package io.filmtime.core.plugin.api

sealed class PluginAuthState {
  data object NotRequired : PluginAuthState()
  data object Authenticated : PluginAuthState()
  data object NotAuthenticated : PluginAuthState()
  data class Error(val message: String) : PluginAuthState()
}
