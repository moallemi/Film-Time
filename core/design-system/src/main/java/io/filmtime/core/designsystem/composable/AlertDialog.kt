package io.filmtime.core.designsystem.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews

@Composable
fun FilmTimeAlertDialog(
  title: String,
  message: String?,
  confirmText: String,
  confirmLoading: Boolean = false,
  onConfirm: () -> Unit,
  dismissText: String,
  onDismissRequest: () -> Unit,
  properties: DialogProperties = DialogProperties(),
) {
  AlertDialog(
    title = {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
      )
    },
    text = if (message != null) {
      {
        Text(
          text = message,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    } else {
      null
    },
    confirmButton = {
      FilmTimeFilledTonalButton(
        isLoading = confirmLoading,
        onClick = { if (!confirmLoading) onConfirm() },
      ) {
        Text(text = confirmText)
      }
    },
    dismissButton = {
      FilmTimeTextButton(
        enabled = !confirmLoading,
        colors = ButtonDefaults.textButtonColors(),
        onClick = { onDismissRequest() },
      ) {
        Text(text = dismissText)
      }
    },
    onDismissRequest = onDismissRequest,
    properties = properties,
  )
}

@ThemePreviews
@Composable
private fun FilmTimeAlertDialogPreview() {
  PreviewFilmTimeTheme {
    FilmTimeAlertDialog(
      title = "Title",
      message = "Are you sure about that?",
      confirmText = "Yes",
      onConfirm = { },
      dismissText = "Cancel",
      onDismissRequest = { },
    )
  }
}

@ThemePreviews
@Composable
private fun FilmTimeAlertDialogPreviewLongText() {
  PreviewFilmTimeTheme {
    FilmTimeAlertDialog(
      title = "Title",
      message = "Are you sure about that?",
      confirmText = "Allow Access to Location",
      onConfirm = { },
      dismissText = "Maybe Later",
      onDismissRequest = { },
    )
  }
}
