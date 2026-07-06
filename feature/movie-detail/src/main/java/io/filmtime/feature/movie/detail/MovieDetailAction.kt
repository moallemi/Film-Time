package io.filmtime.feature.movie.detail

import io.filmtime.core.plugin.api.PluginMetadata

internal sealed interface MovieDetailAction {
  data object Reload : MovieDetailAction
  data object Play : MovieDetailAction
  data object AddBookmark : MovieDetailAction
  data object RemoveBookmark : MovieDetailAction
  data class SelectPlugin(val plugin: PluginMetadata) : MovieDetailAction
  data object DismissPluginSelection : MovieDetailAction
  data object DismissNoPluginsDialog : MovieDetailAction
  data class PluginLoginResult(val success: Boolean) : MovieDetailAction
}
