package io.filmtime.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StreamInfo(
  val url: String,
  val quality: String? = null,
  val streamType: String? = null,
  val title: String? = null,
  val headers: Map<String, String> = emptyMap(),
  val subtitles: List<SubtitleInfo> = emptyList(),
)

@Serializable
data class SubtitleInfo(
  val url: String,
  val language: String,
  val label: String? = null,
)
