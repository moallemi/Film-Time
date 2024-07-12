package io.filmtime.core.libs.logger

interface Logger {

  fun init(enabled: Boolean)

  fun setUserId(id: String)
}
