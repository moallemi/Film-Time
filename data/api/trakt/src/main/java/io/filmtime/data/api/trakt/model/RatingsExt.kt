package io.filmtime.data.api.trakt.model

import io.filmtime.data.model.RatingInfo
import io.filmtime.data.model.Ratings
import io.filmtime.data.network.trakt.TraktExtendedRatingsResponse
import java.util.Locale

internal fun TraktExtendedRatingsResponse.toRatings() =
  Ratings(
    trakt = trakt?.let {
      val rating = it.rating
      if (rating != null && rating > 0.0) {
        RatingInfo(rating.formatted(), it.votes?.formatted())
      } else {
        null
      }
    },
    tmdb = tmdb?.let {
      val rating = it.rating
      if (rating != null && rating > 0.0) {
        RatingInfo(rating.formatted(), it.votes?.formatted())
      } else {
        null
      }
    },
    imdb = imdb?.let {
      val rating = it.rating
      if (rating != null && rating > 0.0) {
        RatingInfo(rating.formatted(), it.votes?.formatted())
      } else {
        null
      }
    },
    rottenTomatoes = rottenTomatoes?.let {
      val rating = it.rating
      if (rating != null && rating > 0.0) {
        RatingInfo(
          rating = "${rating.toInt()}",
          votes = null,
          info = it.rating?.let { rating ->
            if (rating >= 60) {
              "Fresh"
            } else {
              "Rotten"
            }
          },
        )
      } else {
        null
      }
    },
  )

private fun Double.formatted(): String {
  return String.format(Locale.ENGLISH, "%.1f", this)
}

private fun Long.formatted(): String {
  val thousand = this / 1000
  val million = this / 1_000_000

  return when {
    million >= 1.0 -> "${million}m"
    thousand >= 1.0 -> "${thousand}k"
    else -> this.toString()
  }
}
