package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail

interface GetSimilarShowsUseCase {

  suspend operator fun invoke(movieId: Int): Result<List<VideoThumbnail>, GeneralError>
}
