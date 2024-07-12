package io.filmtime

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.filmtime.core.designsystem.theme.FilmTimeTheme
import io.filmtime.core.libs.tracker.Tracker
import io.filmtime.core.ui.navigation.screenName
import io.filmtime.ui.FilmTimeApp
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  internal lateinit var tracker: Tracker

  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()

    super.onCreate(savedInstanceState)

    setContent {
      val navController = rememberNavController()

      LaunchedEffect(navController, tracker) {
        navController.currentBackStackEntryFlow.collect { entry ->
          tracker.trackScreenView(
            label = entry.screenName,
            route = entry.destination.route,
            arguments = entry.arguments,
          )
        }
      }

      DisposableEffect(Unit) {
        val listener = Consumer<Intent> {
          navController.handleDeepLink(it)
        }
        addOnNewIntentListener(listener)
        onDispose { removeOnNewIntentListener(listener) }
      }

      FilmTimeTheme {
        FilmTimeApp(
          navController = navController,
        )
      }
    }
  }
}
