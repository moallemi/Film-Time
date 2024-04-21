package io.filmtime.core.designsystem.theme

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
fun PreviewFilmTimeTheme(content: @Composable () -> Unit) {
  FilmTimeTheme {
    Surface {
      content()
    }
  }
}
