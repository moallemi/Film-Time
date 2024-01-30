package io.filmtime.data.model

sealed class GeneralError {
  data class ApiError(val message: String?, val code: Int) : GeneralError()
  data object NetworkError : GeneralError()
  data class UnknownError(val error: Throwable) : GeneralError()
}

class GeneralErrorThrowable(
  val generalError: GeneralError,
) : Throwable() {

  override val message: String
    get() = when (generalError) {
      is GeneralError.ApiError -> generalError.message ?: "Unknown API error"
      is GeneralError.NetworkError -> "Network error"
      is GeneralError.UnknownError -> generalError.error.message ?: "Unknown error"
    }
}

fun GeneralError.toThrowable() = GeneralErrorThrowable(this)
