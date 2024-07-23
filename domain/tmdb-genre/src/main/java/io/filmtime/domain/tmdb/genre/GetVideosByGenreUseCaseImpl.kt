package io.filmtime.domain.tmdb.genre

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.data.model.VideoType.Show
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideosByGenreUseCaseImpl @Inject constructor(
  private val tmdbShowsRepository: TmdbShowsRepository,
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetVideosByGenreUseCase {
  override fun invoke(genreId: Long, type: VideoType): Flow<PagingData<VideoThumbnail>> =
    when (type) {
      Movie -> tmdbMovieRepository.moviesByGenre(genreId)
      Show -> tmdbShowsRepository.showsByGenre(genreId)
    }
}
