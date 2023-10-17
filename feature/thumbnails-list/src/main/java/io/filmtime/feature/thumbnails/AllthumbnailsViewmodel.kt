package io.filmtime.feature.thumbnails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.VideoType
import io.filmtime.domain.tmdb.movies.GetTrendingMoviePagingUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class AllthumbnailsViewmodel @Inject constructor(
  private val getTrendingMoviePagingUsecase: GetTrendingMoviePagingUsecase
) : ViewModel(){

  val TrendingMovies =
    getTrendingMoviePagingUsecase().cachedIn(viewModelScope)



}
