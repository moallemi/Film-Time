package io.filmtime.domain.tmdb.shows.model

import io.filmtime.data.tmdb.shows.ShowListType.AiringToday
import io.filmtime.data.tmdb.shows.ShowListType.OnTheAir
import io.filmtime.data.tmdb.shows.ShowListType.Popular
import io.filmtime.data.tmdb.shows.ShowListType.TopRated
import io.filmtime.data.tmdb.shows.ShowListType.Trending

enum class ShowListType {
  Trending,
  Popular,
  TopRated,
  OnTheAir,
  AiringToday,
}

internal fun ShowListType.toDataShowListType() = when (this) {
  ShowListType.Trending -> Trending
  ShowListType.Popular -> Popular
  ShowListType.TopRated -> TopRated
  ShowListType.OnTheAir -> OnTheAir
  ShowListType.AiringToday -> AiringToday
}
