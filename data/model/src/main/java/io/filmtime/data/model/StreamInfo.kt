package io.filmtime.data.model

data class StreamInfo(
  val url: String,
  val quality: String? = null,
  val streamType: String? = null,
  val title: String? = null,
  val headers: Map<String, String> = emptyMap(),
  val subtitles: List<SubtitleInfo> = emptyList(),
)

data class SubtitleInfo(
  val url: String,
  val language: String,
  val label: String? = null,
)
