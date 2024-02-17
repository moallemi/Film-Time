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
import io.filmtime.domain.tmdb.movies.GetTrendingMoviesStreamUseCase
import io.filmtime.domain.tmdb.shows.ObserveShowsStreamUseCase
import io.filmtime.domain.tmdb.shows.model.ShowListType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class VideoThumbnailGridViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val trendingMovies: GetTrendingMoviesStreamUseCase,
  private val trendingShows: ObserveShowsStreamUseCase,
) : ViewModel() {

  private var args = VideoThumbnailGridArgs(savedStateHandle)
  private val videoType = args.videoType
  private val listType = args.listType

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
      VideoType.Movie -> trendingMovies()
      VideoType.Show -> trendingShows(listType.toShowListType())
    }

  private fun VideoListType.toShowListType() = when (this) {
    VideoListType.Trending -> ShowListType.Trending
    VideoListType.Popular -> ShowListType.Popular
    VideoListType.TopRated -> ShowListType.TopRated
    VideoListType.OnTheAir -> ShowListType.OnTheAir
    VideoListType.AiringToday -> ShowListType.AiringToday
  }
}
