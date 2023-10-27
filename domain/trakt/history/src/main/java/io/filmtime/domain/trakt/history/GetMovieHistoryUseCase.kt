package io.filmtime.domain.trakt.history

interface GetMovieHistoryUseCase {

  suspend operator fun invoke(id: String)
}
