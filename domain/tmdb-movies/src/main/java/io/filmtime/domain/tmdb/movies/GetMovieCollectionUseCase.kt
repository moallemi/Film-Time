package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.Result

interface GetMovieCollectionUseCase {

  suspend operator fun invoke(collectionId: Int): Result<MovieCollection, GeneralError>
}
