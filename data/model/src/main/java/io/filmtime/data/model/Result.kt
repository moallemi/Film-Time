package io.filmtime.data.model

sealed class Result<out S, out E> {
  data class Success<out S>(val data: S) : Result<S, Nothing>()
  data class Failure<out E>(val error: E) : Result<Nothing, E>()

  suspend fun <T> mapSuccess(transform: suspend (S) -> T): Result<T, E> {
    return when (this) {
      is Result.Failure -> this
      is Success -> Success(transform(data))
    }
  }

  suspend fun <T> mapFailure(transform: suspend (E) -> T): Result<S, T> {
    return when (this) {
      is Result.Failure -> Failure(transform(error))
      is Success -> this
    }
  }

  inline fun fold(
    onSuccess: (S) -> Unit,
    onFailure: (E) -> Unit,
    onCompletion: () -> Unit = {},
  ) {
    when (this) {
      is Result.Failure -> onFailure(this.error)
      is Success -> onSuccess(this.data)
    }

    onCompletion()
  }

  fun successValue(): S? {
    return when (this) {
      is Success -> this.data
      is Result.Failure -> null
    }
  }

  fun errorValue(): E? {
    return when (this) {
      is Success -> null
      is Result.Failure -> this.error
    }
  }
}
