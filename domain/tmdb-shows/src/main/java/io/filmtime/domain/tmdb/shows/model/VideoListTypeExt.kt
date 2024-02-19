package io.filmtime.domain.tmdb.shows.model

import io.filmtime.data.model.VideoListType
import io.filmtime.data.tmdb.shows.ShowListType.AiringToday
import io.filmtime.data.tmdb.shows.ShowListType.OnTheAir
import io.filmtime.data.tmdb.shows.ShowListType.Popular
import io.filmtime.data.tmdb.shows.ShowListType.TopRated
import io.filmtime.data.tmdb.shows.ShowListType.Trending

internal fun VideoListType.toShowListType() = when (this) {
  VideoListType.Trending -> Trending
  VideoListType.Popular -> Popular
  VideoListType.TopRated -> TopRated
  VideoListType.NowPlaying -> throw IllegalArgumentException("Invalid type for show list")
  VideoListType.Upcoming -> throw IllegalArgumentException("Invalid type for show list")
  VideoListType.OnTheAir -> OnTheAir
  VideoListType.AiringToday -> AiringToday
}
