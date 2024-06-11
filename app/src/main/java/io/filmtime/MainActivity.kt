package io.filmtime

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.filmtime.core.designsystem.theme.FilmTimeTheme
import io.filmtime.ui.FilmTimeApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()

    super.onCreate(savedInstanceState)

    setContent {
      val navController = rememberNavController()

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
