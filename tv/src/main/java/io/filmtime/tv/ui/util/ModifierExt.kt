package io.filmtime.tv.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme
import io.filmtime.core.ui.common.componnents.placeholder.PlaceholderHighlight
import io.filmtime.core.ui.common.componnents.placeholder.fade
import io.filmtime.core.ui.common.componnents.placeholder.placeholder

@Composable
fun Modifier.fadingPlaceholder(
  visible: Boolean = true,
  color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = .1f),
) =
  placeholder(
    visible = visible,
    color = color,
    highlight = PlaceholderHighlight.fade(),
  )
