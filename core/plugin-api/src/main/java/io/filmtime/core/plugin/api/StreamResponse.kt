package io.filmtime.core.plugin.api

data class StreamResponse(
  val streams: List<PluginStream>,
)

data class PluginStream(
  val url: String,
  val quality: String,
  val streamType: String,
  val title: String?,
  val headers: Map<String, String>,
  val subtitles: List<PluginSubtitle>,
)

data class PluginSubtitle(
  val url: String,
  val language: String,
  val label: String?,
)
