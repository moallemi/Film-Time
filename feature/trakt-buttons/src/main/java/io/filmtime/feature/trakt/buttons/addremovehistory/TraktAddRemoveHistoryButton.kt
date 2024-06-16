package io.filmtime.feature.trakt.buttons.addremovehistory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.designsystem.composable.FilmTimeFilledTonalButton
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.data.model.VideoType

@Composable
fun TraktAddRemoveHistoryButton(
  modifier: Modifier = Modifier,
  videoType: VideoType,
  tmdbId: Int,
) {
  val viewModel = hiltViewModel<TraktHistoryViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(videoType, tmdbId) {
    viewModel.checkIfIsWatched(videoType, tmdbId)
  }

  TraktAddRemoveHistoryButton(
    state = state,
    modifier = modifier,
    onClick = {
      if (state.isWatched) {
        viewModel.removeItemFromHistory(tmdbId)
      } else {
        viewModel.addItemToHistory()
      }
    },
  )
}

@Composable
private fun TraktAddRemoveHistoryButton(
  state: TraktAddRemoveUiState,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  FilmTimeFilledTonalButton(
    isLoading = state.isLoading,
    onClick = onClick,
    enabled = !state.isError,
    title = {
      if (state.isWatched) {
        Text("Mark as unwatched")
      } else {
        Text("Mark as watched")
      }
    },
    leadingIcon = {
      if (state.isWatched) {
        Icon(
          imageVector = Icons.Rounded.PlaylistRemove,
          contentDescription = "Add to history button",
        )
      } else {
        Icon(
          imageVector = Icons.AutoMirrored.Rounded.PlaylistAdd,
          contentDescription = "Remove from history button",
        )
      }
    },
  )
}

@ThemePreviews
@Composable
private fun TraktAddRemoveHistoryButtonPreview() {
  PreviewFilmTimeTheme {
    TraktAddRemoveHistoryButton(
      state = TraktAddRemoveUiState(
        isLoading = false,
      ),
      onClick = {},
    )
  }
}
