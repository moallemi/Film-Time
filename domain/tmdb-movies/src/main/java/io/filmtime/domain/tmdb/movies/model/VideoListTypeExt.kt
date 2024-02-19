package io.filmtime.domain.tmdb.movies.model

import io.filmtime.data.model.VideoListType
import io.fimltime.data.tmdb.movies.MovieListType.NowPlaying
import io.fimltime.data.tmdb.movies.MovieListType.Popular
import io.fimltime.data.tmdb.movies.MovieListType.TopRated
import io.fimltime.data.tmdb.movies.MovieListType.Trending
import io.fimltime.data.tmdb.movies.MovieListType.Upcoming

internal fun VideoListType.toMovieListType() =
  when (this) {
    VideoListType.Trending -> Trending
    VideoListType.Popular -> Popular
    VideoListType.TopRated -> TopRated
    VideoListType.NowPlaying -> NowPlaying
    VideoListType.Upcoming -> Upcoming
    VideoListType.OnTheAir -> throw IllegalArgumentException("Invalid type for movie list")
    VideoListType.AiringToday -> throw IllegalArgumentException("Invalid type for movie list")
  }
