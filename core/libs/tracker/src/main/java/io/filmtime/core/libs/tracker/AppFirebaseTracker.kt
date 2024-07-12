package io.filmtime.core.libs.tracker

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Provider

internal class AppFirebaseTracker @Inject constructor(
  private val firebaseAnalytics: Provider<FirebaseAnalytics>,
  private val firebaseCrashlytics: Provider<FirebaseCrashlytics>,
) : Tracker {

  override fun trackScreenView(
    label: String,
    route: String?,
    arguments: Bundle?,
  ) {
    try {
      firebaseAnalytics.get().logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        param(FirebaseAnalytics.Param.SCREEN_NAME, label)

        if (route != null) {
          param("screen_route", route)
        }

        for (key in arguments?.keySet().orEmpty()) {
          val value = arguments?.get(key)

          // We don't want to include the label or route twice
          if (value != label && value != route) {
            param("arg_$key".asFireBaseParamName(), value.toString().take(MAX_VALUE_LENGTH))
          }
        }
      }
    } catch (_: Throwable) {
      // Ignore, Firebase might not be setup for this project
    }
  }

  override fun trackError(
    exception: Throwable,
  ) {
    try {
      firebaseCrashlytics.get().recordException(exception)
    } catch (_: Throwable) {
      // Ignore, Firebase might not be setup for this project
    }
  }

  private fun String.asFireBaseParamName() =
    take(MAX_KEY_LENGTH)
      .replace(regex = "[^a-zA-Z0-9]".toRegex(), replacement = "_")

  companion object {
    private const val MAX_KEY_LENGTH = 40
    private const val MAX_VALUE_LENGTH = 100
  }
}
