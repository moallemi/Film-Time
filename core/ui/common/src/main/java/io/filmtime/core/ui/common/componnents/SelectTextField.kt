package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews

@Composable
fun SelectTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  enabled: Boolean = true,
  singleLine: Boolean = true,
  shape: Shape = TextFieldDefaults.shape,
  trailingIcon: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  placeHolder: @Composable (() -> Unit)? = null,
) {
  val interactionSource = remember {
    MutableInteractionSource()
  }

  BasicTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    visualTransformation = visualTransformation,
    interactionSource = interactionSource,
    enabled = enabled,
    singleLine = singleLine,
    readOnly = true,
  ) { innerTextField ->

    TextFieldDefaults.DecorationBox(
      value = value,
      visualTransformation = visualTransformation,
      innerTextField = innerTextField,
      singleLine = singleLine,
      enabled = enabled,
      interactionSource = interactionSource,
      contentPadding = PaddingValues(start = 16.dp),
      trailingIcon = trailingIcon,
      placeholder = placeHolder,
      leadingIcon = leadingIcon,
      shape = shape,
      colors = TextFieldDefaults.colors().copy(
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
      ),
    )
  }
}

@ThemePreviews
@Composable
private fun PreviewSelectTextField() {
  PreviewFilmTimeTheme {
    SelectTextField(
      value = "Select",
      onValueChange = {},
    )
  }
}
