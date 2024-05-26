package io.filmtime.core.designsystem.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun FilmTimeFilledButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  colors: ButtonColors = ButtonDefaults.buttonColors(),
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  shape: Shape = ButtonDefaults.shape,
  elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
  content: @Composable RowScope.() -> Unit,
) {
  Button(
    onClick = { if (!isLoading) onClick() },
    modifier = modifier,
    enabled = enabled,
    colors = colors,
    contentPadding = contentPadding,
    shape = shape,
    elevation = elevation,
    content = {
      FilmTimeLoadingButtonContent(
        isLoading = isLoading,
        progressColor = MaterialTheme.colorScheme.onPrimary,
      ) {
        content()
      }
    },
  )
}

@Composable
fun FilmTimeFilledButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  colors: ButtonColors = ButtonDefaults.buttonColors(),
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  shape: Shape = ButtonDefaults.shape,
  elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
  title: @Composable () -> Unit,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  FilmTimeFilledButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colors = colors,
    contentPadding = contentPadding,
    shape = shape,
    elevation = elevation,
  ) {
    FilmTimeLoadingButtonContent(isLoading = isLoading) {
      FilmTimeButtonContent(
        title = title,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
      )
    }
  }
}

@Composable
fun FilmTimeFilledTonalButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  shape: Shape = ButtonDefaults.filledTonalShape,
  elevation: ButtonElevation = ButtonDefaults.filledTonalButtonElevation(),
  title: @Composable () -> Unit,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  FilmTimeFilledButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colors = colors,
    contentPadding = contentPadding,
    shape = shape,
    elevation = elevation,
  ) {
    FilmTimeLoadingButtonContent(isLoading = isLoading) {
      FilmTimeButtonContent(
        title = title,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
      )
    }
  }
}

@Composable
fun FilmTimeFilledTonalButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  shape: Shape = ButtonDefaults.filledTonalShape,
  elevation: ButtonElevation = ButtonDefaults.filledTonalButtonElevation(),
  content: @Composable RowScope.() -> Unit,
) {
  Button(
    onClick = { if (!isLoading) onClick() },
    modifier = modifier,
    enabled = enabled,
    colors = colors,
    contentPadding = contentPadding,
    shape = shape,
    elevation = elevation,
    content = {
      FilmTimeLoadingButtonContent(
        isLoading = isLoading,
        progressColor = MaterialTheme.colorScheme.onPrimary,
      ) {
        content()
      }
    },
  )
}

@Composable
fun FilmTimeOutlinedButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  border: BorderStroke = ButtonDefaults.outlinedButtonBorder,
  colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  shape: Shape = ButtonDefaults.outlinedShape,
  elevation: ButtonElevation? = null,
  content: @Composable RowScope.() -> Unit,
) {
  OutlinedButton(
    onClick = { if (!isLoading) onClick() },
    modifier = modifier,
    enabled = enabled,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    shape = shape,
    elevation = elevation,
    content = {
      FilmTimeLoadingButtonContent(isLoading = isLoading) {
        content()
      }
    },
  )
}

@Composable
fun FilmTimeOutlinedButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  border: BorderStroke = ButtonDefaults.outlinedButtonBorder,
  colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
  contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
  shape: Shape = ButtonDefaults.outlinedShape,
  elevation: ButtonElevation? = null,
  title: @Composable () -> Unit,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  FilmTimeOutlinedButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    shape = shape,
    elevation = elevation,
  ) {
    FilmTimeLoadingButtonContent(isLoading = isLoading) {
      FilmTimeButtonContent(
        title = title,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
      )
    }
  }
}

@Composable
fun FilmTimeTextButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  colors: ButtonColors = ButtonDefaults.textButtonColors(),
  contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
  content: @Composable RowScope.() -> Unit,
) {
  TextButton(
    onClick = { if (!isLoading) onClick() },
    modifier = modifier,
    enabled = enabled,
    shape = ButtonDefaults.textShape,
    colors = colors,
    elevation = null,
    border = null,
    contentPadding = contentPadding,
    content = {
      FilmTimeLoadingButtonContent(isLoading = isLoading) {
        content()
      }
    },
  )
}

@Composable
fun FilmTimeTextButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  colors: ButtonColors = ButtonDefaults.textButtonColors(),
  contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
  title: @Composable () -> Unit,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null,
) {
  FilmTimeTextButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colors = colors,
    contentPadding = contentPadding,
  ) {
    FilmTimeLoadingButtonContent(isLoading = isLoading) {
      FilmTimeButtonContent(
        title = title,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
      )
    }
  }
}

@Composable
fun FilmTimeFloatingActionButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = FloatingActionButtonDefaults.shape,
  containerColor: Color = FloatingActionButtonDefaults.containerColor,
  contentColor: Color = contentColorFor(containerColor),
  elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable () -> Unit,
) {
  FloatingActionButton(
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    containerColor = containerColor,
    contentColor = contentColor,
    elevation = elevation,
    interactionSource = interactionSource,
    content = content,
  )
}

@Composable
fun FilmTimeSmallFloatingActionButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  shape: Shape = FloatingActionButtonDefaults.smallShape,
  containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
  contentColor: Color = contentColorFor(containerColor),
  elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable () -> Unit,
) {
  SmallFloatingActionButton(
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    containerColor = containerColor,
    contentColor = contentColor,
    elevation = elevation,
    interactionSource = interactionSource,
    content = content,
  )
}

@Composable
private fun RowScope.FilmTimeButtonContent(
  title: @Composable () -> Unit,
  leadingIcon: @Composable (() -> Unit)?,
  trailingIcon: @Composable (() -> Unit)?,
) {
  if (leadingIcon != null) {
    Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
      leadingIcon()
    }
  }
  Box(
    Modifier
      .padding(
        start = if (leadingIcon != null) {
          ButtonDefaults.IconSpacing
        } else {
          0.dp
        },
        end = if (trailingIcon != null) {
          ButtonDefaults.IconSpacing
        } else {
          0.dp
        },
      ),
  ) {
    title()
  }
  if (trailingIcon != null) {
    Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
      trailingIcon()
    }
  }
}

@Composable
private fun FilmTimeLoadingButtonContent(
  isLoading: Boolean,
  progressColor: Color = ProgressIndicatorDefaults.circularColor,
  content: @Composable RowScope.() -> Unit,
) {
  val contentAlpha by animateFloatAsState(targetValue = if (isLoading) 0f else 1f)
  val loadingAlpha by animateFloatAsState(targetValue = if (isLoading) 1f else 0f)
  Box(
    contentAlignment = Alignment.Center,
  ) {
    FilmTimeButtonLoadingProgressBar(
      modifier = Modifier.graphicsLayer { alpha = loadingAlpha },
      color = progressColor,
    )
    Box(
      modifier = Modifier.graphicsLayer { alpha = contentAlpha },
    ) {
      Row {
        content()
      }
    }
  }
}

@Composable
private fun FilmTimeButtonLoadingProgressBar(
  modifier: Modifier = Modifier,
  color: Color = ProgressIndicatorDefaults.circularColor,
) {
  FilmTimeCircularProgressBar(
    modifier = modifier
      .size(24.dp),
    strokeWidth = 3.dp,
    color = color,
  )
}
