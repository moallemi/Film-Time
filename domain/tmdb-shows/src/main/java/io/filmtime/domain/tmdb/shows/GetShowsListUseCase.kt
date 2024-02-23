package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface GetShowsListUseCase {

  suspend operator fun invoke(videoListType: VideoListType): Flow<Result<List<VideoThumbnail>, GeneralError>>
}
