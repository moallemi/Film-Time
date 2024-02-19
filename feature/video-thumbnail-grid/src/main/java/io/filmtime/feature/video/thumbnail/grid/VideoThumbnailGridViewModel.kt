package io.filmtime.feature.video.thumbnail.grid

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType
import io.filmtime.domain.tmdb.movies.ObserveMoviesStreamUseCase
import io.filmtime.domain.tmdb.shows.ObserveShowsStreamUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class VideoThumbnailGridViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val observeMoviesStream: ObserveMoviesStreamUseCase,
  private val observeShowsStream: ObserveShowsStreamUseCase,
) : ViewModel() {

  private val args = VideoThumbnailGridArgs(savedStateHandle)
  private val videoType = args.videoType
  private val listType = args.listType

  val state = MutableStateFlow(
    VideoThumbnailGridUiState(
      title = generateTitle(),
    ),
  )

  val pagedList: Flow<PagingData<VideoThumbnail>> =
    loadVideoThumbnails()
      .map { pagingData ->
        val items = mutableListOf<VideoThumbnail>()
        pagingData.filter { videoThumbnail ->
          items.contains(videoThumbnail)
            .not()
            .also { shouldAdd ->
              if (shouldAdd) {
                items.add(videoThumbnail)
              }
            }
        }
      }
      .cachedIn(viewModelScope)

  private fun loadVideoThumbnails(): Flow<PagingData<VideoThumbnail>> =
    when (videoType) {
      VideoType.Movie -> observeMoviesStream(listType)
      VideoType.Show -> observeShowsStream(listType)
    }

  private fun generateTitle(): String {
    val listType = when (listType) {
      VideoListType.Trending -> "Trending"
      VideoListType.Popular -> "Popular"
      VideoListType.TopRated -> "Top Rated"
      VideoListType.NowPlaying -> "Now Playing"
      VideoListType.Upcoming -> "Upcoming"
      VideoListType.OnTheAir -> "On The Air"
      VideoListType.AiringToday -> "Airing Today"
    }
    return when (videoType) {
      VideoType.Movie -> "$listType Movies"
      VideoType.Show -> "$listType Shows"
    }
  }
}
