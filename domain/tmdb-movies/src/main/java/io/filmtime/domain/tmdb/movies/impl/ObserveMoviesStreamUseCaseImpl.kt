package io.filmtime.domain.tmdb.movies.impl

import androidx.paging.PagingData
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.domain.tmdb.movies.ObserveMoviesStreamUseCase
import io.filmtime.domain.tmdb.movies.model.toMovieListType
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ObserveMoviesStreamUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : ObserveMoviesStreamUseCase {

  override fun invoke(videoListType: VideoListType): Flow<PagingData<VideoThumbnail>> =
    tmdbMovieRepository.moviesStream(videoListType.toMovieListType())
}
