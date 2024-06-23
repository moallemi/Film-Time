package io.filmtime.core.ui.common.componnents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.filmtime.core.designsystem.composable.FilmTimeFilledTonalButton
import io.filmtime.core.designsystem.composable.FilmTimeOutlinedButton

@Composable
fun BookmarkButton(
  isBookmarked: Boolean,
  onRemoveBookmark: () -> Unit,
  onAddBookmark: () -> Unit,
  modifier: Modifier = Modifier,
) {
  if (isBookmarked) {
    FilmTimeOutlinedButton(
      modifier = modifier,
      onClick = onRemoveBookmark,
      title = { Text("Bookmarked") },
      leadingIcon = {
        Icon(
          imageVector = Icons.Filled.Check,
          contentDescription = "Check icon",
        )
      },
    )
  } else {
    FilmTimeFilledTonalButton(
      modifier = modifier,
      onClick = onAddBookmark,
      title = { Text("Bookmark") },
      leadingIcon = {
        Icon(
          imageVector = Icons.Filled.Add,
          contentDescription = "Add icon",
        )
      },
    )
  }
}
