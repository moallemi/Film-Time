package io.filmtime.tv.ui.component

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import io.filmtime.tv.ui.credits.CreditsAction
import io.filmtime.tv.ui.credits.CreditsViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CastsRow(
  modifier: Modifier = Modifier,
  type: VideoType = VideoType.Movie,
  tmdbId: Int,
  onPersonClick: (Person) -> Unit = {},
) {
  val (lazyRow, firstItem) = remember { FocusRequester.createRefs() }
  val viewModel = hiltViewModel<CreditsViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(tmdbId) {
    viewModel.submitAction(CreditsAction.LoadCredits(tmdbId, type))
  }
  Column(modifier.focusGroup()) {
    Text(
      text = stringResource(R.string.cast_crew),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(horizontal = 60.dp, vertical = 20.dp),
    )
    LazyRow(
      modifier = Modifier
        .focusRequester(lazyRow)
        .focusRestorer { firstItem },
      contentPadding = PaddingValues(
        horizontal = 30.dp,
      ),
      horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
      itemsIndexed(state.credit) { index, item ->
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
