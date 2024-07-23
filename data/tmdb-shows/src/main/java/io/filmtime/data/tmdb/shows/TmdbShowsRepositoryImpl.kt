package io.filmtime.data.tmdb.shows

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.filmtime.data.api.tmdb.TmdbShowsRemoteSource
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TmdbShowsRepositoryImpl @Inject constructor(
  private val tmdbShowsRemoteSource: TmdbShowsRemoteSource,
) : TmdbShowsRepository {

  override suspend fun showDetails(showId: Int): Result<VideoDetail, GeneralError> =
    tmdbShowsRemoteSource.showDetails(showId)

  override suspend fun trendingShows(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.trendingShows(page = 1)

  override suspend fun popularShows(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.popularShows(page = 1)

  override suspend fun topRatedShows(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.topRatedShows(page = 1)

  override suspend fun onTheAirShows(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.onTheAirShows(page = 1)

  override suspend fun airingTodayShows(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.airingTodayShows(page = 1)

  override fun showsStream(
    type: ShowListType,
  ): Flow<PagingData<VideoThumbnail>> =
    Pager(
      config = PagingConfig(
        pageSize = TmdbShowsRemoteSource.PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        ShowsPagingSource(
          tmdbShowsRemoteSource = tmdbShowsRemoteSource,
          showListType = type,
        )
      },
    ).flow

  override fun showsByGenre(genreId: Long): Flow<PagingData<VideoThumbnail>> =
    Pager(
      config = PagingConfig(
        pageSize = TmdbShowsRemoteSource.PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        ShowsByGenrePagingSource(
          tmdbShowsRemoteSource = tmdbShowsRemoteSource,
          genreId = genreId,
        )
      },
    ).flow

  override suspend fun credits(movieId: Int): Result<List<Person>, GeneralError> =
    tmdbShowsRemoteSource.credits(movieId)

  override suspend fun similar(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.similar(movieId)

  override suspend fun episodesBySeason(showId: Int, seasonNumber: Int): Result<List<EpisodeThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.episodesBySeason(showId, seasonNumber)
}
