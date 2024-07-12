package io.filmtime.core.libs.tracker

import android.os.Bundle

interface Tracker {

  fun trackScreenView(
    label: String,
    route: String?,
    arguments: Bundle? = null,
  )

  fun trackError(
    exception: Throwable,
  )
}
