package io.filmtime.core.plugin.api

sealed class StreamRequest {

  data class Movie(
    val tmdbId: Int,
    val imdbId: String?,
    val title: String,
    val year: Int,
  ) : StreamRequest()

  data class Show(
    val tmdbId: Int,
    val imdbId: String?,
    val title: String,
    val year: Int,
    val season: Int,
    val episode: Int,
  ) : StreamRequest()
}
