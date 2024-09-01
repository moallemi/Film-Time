package io.filmtime.data.model

enum class ExternalProvider {
  IMDB, TRAKT, SLUG, TMDB,
}


data class ExternalID(
  val traktId: String?,
  val tmdbId: String?,
  val imdbId: String?,
  val slug: String?,
)
