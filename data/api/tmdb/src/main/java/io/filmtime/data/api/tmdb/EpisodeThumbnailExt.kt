package io.filmtime.data.api.tmdb

import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.VideoId
import io.filmtime.data.network.model.TmdbEpisodeResponse

fun TmdbEpisodeResponse.toEpisodeThumbnail() = EpisodeThumbnail(
  ids = VideoId(
    traktId = null,
    tmdbId = id,
  ),
  episodeNumber = episodeNumber,
  title = name ?: "",
  description = overview ?: "",
  posterUrl = TMDB_BASE_IMAGE_URL.plus(stillPath),
  airDate = airDate.orEmpty(),
)
