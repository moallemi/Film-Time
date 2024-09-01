package io.filmtime.data.trakt

import io.filmtime.data.api.trakt.TraktRemoteSource
import io.filmtime.data.api.trakt.TraktSearchRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoType
import io.filmtime.data.trakt.model.toTraktMediaType
import javax.inject.Inject




internal class TraktRepositoryImpl @Inject constructor(
  private val traktRemoteSource: TraktRemoteSource,
  private val traktSearchRemoteSource: TraktSearchRemoteSource,
) : TraktRepository {

  companion object {
    const val TRAKT_MOVIE_BASE_URL = "https://trakt.tv/movies/"
    const val TRAKT_SHOWS_BASE_URL = ""
    const val IMDB_MOVIE = ""
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
                link = "https://trakt.tv/movies/${traktIdResult.data.traktId}",
              ),
              tmdb = ratings.tmdb?.copy(
                link = "https://www.themoviedb.org/movie/${traktIdResult.data.tmdbId}",
              ),
              imdb = ratings.imdb?.copy(
                link = "https://www.imdb.com/title/${traktIdResult.data.imdbId}",
              ),
            )
          }
      }

      is Result.Failure -> traktIdResult
    }
}
