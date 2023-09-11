package io.filmtime.data.network.adapter

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

internal class NetworkResponseCall<S : Any, E : Any>(
  private val delegate: Call<S>,
  private val errorConverter: Converter<ResponseBody, E>,
) : Call<NetworkResponse<S, E>> {

  override fun enqueue(callback: Callback<NetworkResponse<S, E>>) = delegate.enqueue(
    object : Callback<S> {
      override fun onResponse(call: Call<S>, response: Response<S>) {
        val body = response.body()
        val code = response.code()
        val error = response.errorBody()

        if (response.isSuccessful) {
          callback.onResponse(
            this@NetworkResponseCall,
            Response.success(NetworkResponse.Success(body)),
          )
        } else {
          val errorBody = when {
            error == null -> null
            error.contentLength() == 0L -> null
            else -> try {
              errorConverter.convert(error)
            } catch (ex: Exception) {
              null
            }
          }
          if (errorBody != null) {
            callback.onResponse(
              this@NetworkResponseCall,
              Response.success(NetworkResponse.ApiError(errorBody, code)),
            )
          } else {
            callback.onResponse(
              this@NetworkResponseCall,
              Response.success(
                NetworkResponse.UnknownError(
                  Exception("No error body"),
                ),
              ),
            )
          }
        }
      }

      override fun onFailure(call: Call<S>, throwable: Throwable) {
        val networkResponse = when (throwable) {
          is IOException -> NetworkResponse.NetworkError(throwable)
          else -> NetworkResponse.UnknownError(throwable)
        }
        callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse))
      }
    },
  )

  override fun isExecuted() = delegate.isExecuted

  override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

  override fun isCanceled() = delegate.isCanceled

  override fun cancel() = delegate.cancel()

  override fun execute(): Response<NetworkResponse<S, E>> {
    delegate.execute().let { response ->
      val body = response.body()
      val code = response.code()
      val error = response.errorBody()

      return if (response.isSuccessful) {
        Response.success(NetworkResponse.Success(body))
      } else {
        val errorBody = when {
          error == null -> null
          error.contentLength() == 0L -> null
          else -> try {
            errorConverter.convert(error)
          } catch (ex: Exception) {
            null
          }
        }
        if (errorBody != null) {
          Response.success(NetworkResponse.ApiError(errorBody, code))
        } else {
          Response.success(
            NetworkResponse.UnknownError(
              Exception("No error body"),
            ),
          )
        }
      }
    }
  }

  override fun request(): Request =
    delegate.request()

  override fun timeout(): Timeout =
    delegate.timeout()
}
