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

  override suspend fun ratings(type: VideoType, tmdbId: Int): Result<Ratings, GeneralError> =
    when (val traktIdResult = traktSearchRemoteSource.getByTmdbId(id = tmdbId, type = type.toTraktMediaType())) {
      is Result.Success -> traktRemoteSource.ratings(
        type = "${type.toTraktMediaType().queryName}s",
        traktId = traktIdResult.data,
      )
        .mapSuccess { ratings -> ratings }

      is Result.Failure -> traktIdResult
    }
}
