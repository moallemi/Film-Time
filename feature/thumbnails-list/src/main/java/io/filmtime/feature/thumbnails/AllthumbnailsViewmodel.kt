package io.filmtime.feature.thumbnails


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.domain.tmdb.movies.GetTrendingMoviePagingUsecase
import io.filmtime.domain.tmdb.shows.GetTrendingShowsPagingUsecase
import javax.inject.Inject
@HiltViewModel
class AllthumbnailsViewmodel @Inject constructor(
  getTrendingMoviePagingUsecase: GetTrendingMoviePagingUsecase,
  getTrendingShowsPagingUsecase: GetTrendingShowsPagingUsecase
) : ViewModel(){

  val TrendingMovies =
    getTrendingMoviePagingUsecase.invoke().cachedIn(viewModelScope)

  val TrendingShows =
    getTrendingShowsPagingUsecase.invoke().cachedIn(viewModelScope)

}
