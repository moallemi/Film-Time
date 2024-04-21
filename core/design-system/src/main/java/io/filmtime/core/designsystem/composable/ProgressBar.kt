package io.filmtime.core.designsystem.composable

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun FilmTimeCircularProgressBar(
  modifier: Modifier = Modifier,
  color: Color = ProgressIndicatorDefaults.circularColor,
  strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
) {
  CircularProgressIndicator(
    modifier = modifier,
    color = color,
    strokeWidth = strokeWidth,
  )
}
