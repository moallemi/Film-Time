package io.filmtime.data.network.adapter

import java.io.IOException

sealed class NetworkResponse<out S : Any, out E : Any> {

  data class Success<S : Any>(val body: S?) : NetworkResponse<S, Nothing>()
  data class ApiError<E : Any>(val body: E, val code: Int) : NetworkResponse<Nothing, E>()
  data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()
  data class UnknownError(val error: Throwable) : NetworkResponse<Nothing, Nothing>()
}
