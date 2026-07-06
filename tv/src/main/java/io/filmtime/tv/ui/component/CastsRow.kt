package io.filmtime.tv.ui.component

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import io.filmtime.data.model.Person
import io.filmtime.data.model.VideoType
import io.filmtime.tv.R
import io.filmtime.tv.ui.credits.CreditsUiState
import io.filmtime.tv.ui.credits.CreditsViewModel
import io.filmtime.tv.ui.util.fadingPlaceholder

@Composable
fun CastsRow(
  modifier: Modifier = Modifier,
  type: VideoType = VideoType.Movie,
  tmdbId: Int,
  onPersonClick: (Person) -> Unit = {},
) {
  val viewModel = hiltViewModel<CreditsViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(tmdbId) {
    viewModel.loadCredits(tmdbId, type)
  }
  CastRowContent(
    modifier = modifier,
    uiState = state,
    onPersonClick = onPersonClick,
  )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CastRowContent(
  modifier: Modifier = Modifier,
  uiState: CreditsUiState,
  onPersonClick: (Person) -> Unit,
) {
  val (lazyRow, firstItem) = remember { FocusRequester.createRefs() }
  Column(modifier.focusGroup()) {
    Text(
      text = stringResource(R.string.cast_crew),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(
        horizontal = 60.dp,
        vertical = 20.dp,
      ),
    )
    if (uiState.isLoading) {
      CastRowLoading()
    } else {
      LazyRow(
        modifier = Modifier
          .focusRequester(lazyRow)
          .focusRestorer { firstItem },
        contentPadding = PaddingValues(
          horizontal = 30.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
      ) {
        itemsIndexed(uiState.credit) { index, item ->
          val itemModifier = if (index == 0) {
            Modifier.focusRequester(firstItem)
          } else {
            Modifier
          }
          CastItem(
            item = item,
            modifier = itemModifier.width(100.dp),
            onClick = { onPersonClick(item) },
          )
        }
      }
    }
  }
}

@Composable
fun CastRowLoading(count: Int = 20) {
  LazyRow(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(
      horizontal = 30.dp,
    ),
    horizontalArrangement = Arrangement.spacedBy(20.dp),
  ) {
    items(count = count) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(
          modifier = Modifier
            .clip(CircleShape)
            .size(80.dp)
            .fadingPlaceholder(),
        )
        Column(
          modifier = Modifier.padding(top = 20.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically,
          ),
        ) {
          Spacer(
            modifier = Modifier
              .size(
                width = 50.dp,
                height = 15.dp,
              )
              .fadingPlaceholder(),
          )
          Spacer(
            modifier = Modifier
              .size(
                width = 40.dp,
                height = 10.dp,
              )
              .fadingPlaceholder(),
          )
        }
      }
    }
  }
}
