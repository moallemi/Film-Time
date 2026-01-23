package io.filmtime.core.plugin.api

data class PluginMetadata(
  val pluginId: String,
  val name: String,
  val description: String,
  val version: String,
  val iconUrl: String?,
  val authority: String,
  val packageName: String = "",
  val requiresAuth: Boolean = false,
  val loginActivityClass: String? = null,
)
