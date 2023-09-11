package io.filmtime.data.model

sealed class Result<out S, out E> {
  data class Success<out S>(val data: S) : Result<S, Nothing>()
  data class Failure<out E>(val error: E) : Result<Nothing, E>()
}
