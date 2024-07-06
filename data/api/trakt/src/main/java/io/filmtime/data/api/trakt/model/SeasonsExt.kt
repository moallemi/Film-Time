package io.filmtime.data.api.trakt.model

import io.filmtime.data.model.TraktEpisodeHistory
import io.filmtime.data.network.trakt.TraktExtendedSeasonResponse

fun List<TraktExtendedSeasonResponse>.toSeasons(): Map<Int, List<TraktEpisodeHistory>> =
  associate { season ->
    season.number to (
      season.episodes?.map { episode ->
        TraktEpisodeHistory(
          traktId = episode.ids.trakt ?: 0,
          seasonNumber = season.number,
          episodeNumber = episode.number,
        )
      } ?: emptyList()
      )
  }
