package io.filmtime.tv.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.WideButtonDefaults
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.tv.R
import io.filmtime.tv.ui.util.fadingPlaceholder
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailHeader(
  videoDetail: VideoDetail,
  modifier: Modifier = Modifier,
  isBookmark: Boolean,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
) {
  val viewRequester = remember { BringIntoViewRequester() }
  val coroutineScope = rememberCoroutineScope()
  Column(
    modifier = modifier
      .bringIntoViewRequester(viewRequester),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Text(
      text = videoDetail.title,
      style = MaterialTheme.typography.displayMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface,
    )
    Row(
      horizontalArrangement = Arrangement.spacedBy(
        space = 8.dp,
        alignment = Alignment.CenterHorizontally,
      ),
    ) {
      TextsRowWithSeparator(
        texts = listOf(
          videoDetail.genres.firstOrNull()?.name ?: stringResource(R.string.unknown_genre),
          videoDetail.year.toString().ifEmpty { stringResource(R.string.unknown_year) },
          videoDetail.runtime ?: stringResource(R.string.unknown_time),
        ),
      )
    }
    Text(
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = .9f),
      text = videoDetail.description,
      modifier = Modifier.fillMaxWidth(.4f),
      style = MaterialTheme.typography.bodyMedium,
    )
    ActionButtons(
      primaryButton = {
        Button(
          onClick = {},
          shape = WideButtonDefaults.shape(),
          modifier = Modifier
            .onFocusChanged {
              if (it.isFocused) {
                coroutineScope.launch {
                  viewRequester.bringIntoView()
                }
              }
            },
        ) {
          Icon(imageVector = Rounded.PlayArrow, contentDescription = "")
          Text(text = stringResource(R.string.btn_play))
        }
      },
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        TvBookmarkButton(
          isBookmark = isBookmark,
          onRemove = onRemoveBookmark,
          onAdd = onAddBookmark,
          modifier = Modifier
            .onFocusChanged {
              if (it.isFocused) {
                coroutineScope.launch {
                  viewRequester.bringIntoView()
                }
              }
            },
        )
        WatchHistoryButton(
          modifier = Modifier.onFocusChanged {
            if (it.isFocused) {
              coroutineScope.launch {
                viewRequester.bringIntoView()
              }
            }
          },
          tmdbId = videoDetail.ids.tmdbId ?: 0,
          videoType = Movie,
        )
      }
    }
  }
}

@Composable
fun DetailHeaderLoadingPlaceholder(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Spacer(
      modifier = Modifier
        .size(
          width = 320.dp,
          height = 30.dp,
        )
        .fadingPlaceholder(),
    )
    Spacer(
      modifier = Modifier
        .size(
          width = 220.dp,
          height = 70.dp,
        )
        .fadingPlaceholder(),
    )
    ActionButtons(
      primaryButton = {
        Spacer(
          modifier = Modifier
            .size(
              width = 100.dp,
              height = 30.dp,
            )
            .fadingPlaceholder(),
        )
      },
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Spacer(
          modifier = Modifier
            .size(
              width = 140.dp,
              height = 30.dp,
            )
            .fadingPlaceholder(),
        )
        Spacer(
          modifier = Modifier
            .size(
              width = 140.dp,
              height = 30.dp,
            )
            .fadingPlaceholder(),
        )
      }
    }
  }
}

@Composable
private fun ActionButtons(
  modifier: Modifier = Modifier,
  primaryButton: @Composable () -> Unit,
  secondary: @Composable () -> Unit,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    primaryButton()
    secondary()
  }
}
