package io.filmtime.domain.trakt.auth

interface LogoutTraktUseCase {

  suspend operator fun invoke()
}
