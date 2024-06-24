package io.filmtime.data.trakt.model

import io.filmtime.data.api.trakt.TraktMediaType
import io.filmtime.data.model.VideoType

fun VideoType.toTraktMediaType(): TraktMediaType = when (this) {
  VideoType.Movie -> TraktMediaType.Movie
  VideoType.Show -> TraktMediaType.Show
}
