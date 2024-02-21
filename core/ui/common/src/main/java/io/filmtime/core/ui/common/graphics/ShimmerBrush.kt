package io.filmtime.core.ui.common.graphics

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
  return if (showShimmer) {
    val shimmerColors = listOf(
      Color.LightGray.copy(alpha = 0.4f),
      Color.LightGray.copy(alpha = 0.2f),
      Color.LightGray.copy(alpha = 0.4f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
      initialValue = 0f,
      targetValue = targetValue,
      animationSpec = infiniteRepeatable(
        animation = tween(800),
        repeatMode = RepeatMode.Restart,
      ),
      label = "shimmer-value",
    )
    Brush.linearGradient(
      colors = shimmerColors,
      start = Offset.Zero,
      end = Offset(x = translateAnimation.value, y = translateAnimation.value),
    )
  } else {
    Brush.linearGradient(
      colors = listOf(Color.Transparent, Color.Transparent),
      start = Offset.Zero,
      end = Offset.Zero,
    )
  }
}
