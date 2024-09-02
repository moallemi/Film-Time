package io.filmtime.data.trakt

import io.filmtime.data.api.trakt.TraktRemoteSource
import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoType
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.data.model.VideoType.Show
import io.filmtime.data.trakt.model.toTraktMediaType
import javax.inject.Inject

internal class TraktRepositoryImpl @Inject constructor(
  private val traktRemoteSource: TraktRemoteSource,
  private val traktSearchRemoteSource: TraktSearchRemoteSource,
) : TraktRepository {

  companion object {
    const val TRAKT_MOVIE_BASE_URL = "https://trakt.tv/movies/"
    const val TRAKT_SHOWS_BASE_URL = "https://trakt.tv/shows/"
    const val IMDB_MOVIE_BASE_URL = "https://imdb.com/title/"
    const val TMDB_MOVIE_BASE_URL = "https://themoviedb.org/movie/"
    const val TMDB_SHOW_BASE_URL = "https://themoviedb.org/tv/"
  }

  override suspend fun ratings(type: VideoType, tmdbId: Int): Result<Ratings, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getOtherWebsiteIds(id = tmdbId, type = type.toTraktMediaType())) {
      is Result.Success -> {
        if (traktIdResult.data.traktId == null) Result.Failure("traktId is null")
        traktRemoteSource.ratings(
          type = "${type.toTraktMediaType().queryName}s",
          traktId = traktIdResult.data.traktId!!.toInt(),
        )
          .mapSuccess { ratings ->
            ratings.copy(
              trakt = ratings.trakt?.copy(
                link = when (type) {
                  Movie -> TRAKT_MOVIE_BASE_URL
                  Show -> TRAKT_SHOWS_BASE_URL
                } + traktIdResult.data.traktId,
              ),
              tmdb = ratings.tmdb?.copy(
                link = when (type) {
                  Movie -> TMDB_MOVIE_BASE_URL
                  Show -> TMDB_SHOW_BASE_URL
                } + traktIdResult.data.tmdbId,
              ),
              imdb = ratings.imdb?.copy(
                link = IMDB_MOVIE_BASE_URL + traktIdResult.data.imdbId,
              ),
            )
          }
      }

      is Result.Failure -> traktIdResult
    }
}
