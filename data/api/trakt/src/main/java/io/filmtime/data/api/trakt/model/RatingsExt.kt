package io.filmtime.data.api.trakt.model

import android.icu.text.NumberFormat
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

private val numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH)
private fun Long.formatted(): String {
  return numberFormat.format(this)
}
