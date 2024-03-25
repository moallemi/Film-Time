package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface GetSimilarUseCase {

  suspend operator fun invoke(movieId:Int): Result<List<VideoThumbnail>, GeneralError>
}
