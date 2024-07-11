package io.filmtime.domain.tmdb.movies.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.Result
import io.filmtime.domain.tmdb.movies.GetMovieCollectionUseCase
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import javax.inject.Inject

internal class GetMovieCollectionUseCaseImpl @Inject constructor(
  private val repository: TmdbMovieRepository,
) : GetMovieCollectionUseCase {
  override suspend fun invoke(collectionId: Int): Result<MovieCollection, GeneralError> =
    repository.getCollection(collectionId)
}
