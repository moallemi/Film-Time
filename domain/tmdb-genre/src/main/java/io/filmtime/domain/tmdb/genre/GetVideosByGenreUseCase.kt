package io.filmtime.domain.tmdb.genre

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType
import kotlinx.coroutines.flow.Flow

interface GetVideosByGenreUseCase {

  operator fun invoke(genreId: Long, type: VideoType): Flow<PagingData<VideoThumbnail>>
}
