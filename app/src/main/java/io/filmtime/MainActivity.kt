package io.filmtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.filmtime.core.designsystem.theme.FilmTimeTheme
import io.filmtime.ui.FilmTimeApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    setContent {
      FilmTimeTheme {
        FilmTimeApp(
          navController = rememberNavController(),
        )
      }
    }
  }
}
