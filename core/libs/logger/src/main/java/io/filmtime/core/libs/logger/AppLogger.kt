package io.filmtime.core.libs.logger

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

internal class AppLogger @Inject constructor() : Logger {

  private val crashlytics by lazy { Firebase.crashlytics }

  override fun init(enabled: Boolean) {
    crashlytics.setCrashlyticsCollectionEnabled(enabled)
  }

  override fun setUserId(id: String) {
    crashlytics.setUserId(id)
  }
}
