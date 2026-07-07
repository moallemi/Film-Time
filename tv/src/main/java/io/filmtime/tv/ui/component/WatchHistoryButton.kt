package io.filmtime.tv.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import androidx.tv.material3.WideButtonDefaults
import io.filmtime.data.model.VideoType
import io.filmtime.tv.R
import io.filmtime.tv.ui.traktbutton.TraktAddRemoveAction
import io.filmtime.tv.ui.traktbutton.TraktAddRemoveUiState
import io.filmtime.tv.ui.traktbutton.TraktMovieHistoryViewModel

@Composable
fun WatchHistoryButton(
  modifier: Modifier = Modifier,
  tmdbId: Int,
  videoType: VideoType,
) {
  val viewModel: TraktMovieHistoryViewModel = hiltViewModel()
  val uiState by viewModel.state.collectAsStateWithLifecycle()
  LaunchedEffect(videoType, tmdbId) {
    viewModel.submitAction(TraktAddRemoveAction.CheckIfWatched(videoType, tmdbId))
  }
  WatchHistoryButtonContent(
    modifier = modifier,
    uiState = uiState,
    onRemove = { viewModel.submitAction(TraktAddRemoveAction.RemoveFromHistory) },
    onAdd = { viewModel.submitAction(TraktAddRemoveAction.AddToHistory) },
  )
}

@Composable
fun WatchHistoryButtonContent(
  modifier: Modifier = Modifier,
  uiState: TraktAddRemoveUiState,
  onRemove: () -> Unit,
  onAdd: () -> Unit,
) {
  Button(
    shape = WideButtonDefaults.shape(),
    modifier = modifier,
    enabled = !uiState.isError,
    onClick = {
      if (uiState.isWatched) {
        onRemove()
      } else {
        onAdd()
      }
    },
    content = {
      if (uiState.isLoading) {
        Text(text = stringResource(R.string.loading))
      } else {
        AnimatedContent(
          targetState = uiState.isWatched,
          label = "",
        ) { isWatchedState ->
          if (isWatchedState) {
            Icon(
              imageVector = Rounded.PlaylistRemove,
              contentDescription = stringResource(R.string.cd_unwatched_icon),
            )
          } else {
            Icon(
              imageVector = AutoMirrored.Rounded.PlaylistAdd,
              contentDescription = stringResource(R.string.cd_watched_icon),
            )
          }
        }
        Text(
          text = if (uiState.isWatched) {
            stringResource(R.string.mark_as_unwatched)
          } else {
            stringResource(R.string.mark_as_watched)
          },
        )
      }
    },
  )
}
