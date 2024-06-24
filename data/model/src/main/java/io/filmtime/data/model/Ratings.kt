package io.filmtime.data.model

data class Ratings(
  val trakt: RatingInfo? = null,
  val tmdb: RatingInfo? = null,
  val imdb: RatingInfo? = null,
  val rottenTomatoes: RatingInfo? = null,
) {
  companion object
}

data class RatingInfo(
  val rating: String? = null,
  val votes: String? = null,
  val info: String? = null,
)

val Ratings.Companion.Preview: Ratings
  get() = Ratings(
    trakt = RatingInfo(rating = "7.5", votes = "100"),
    tmdb = RatingInfo(rating = "7.5", votes = "100"),
    imdb = RatingInfo(rating = "7.5", votes = "100"),
    rottenTomatoes = RatingInfo(rating = "7.5", votes = "100", info = "Fresh"),
  )
