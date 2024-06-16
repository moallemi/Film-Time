package io.filmtime.feature.credits.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.R
import io.filmtime.core.ui.common.UiMessage
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.data.model.Person
import io.filmtime.data.model.PreviewCast
import io.filmtime.data.model.PreviewCrew
import io.filmtime.data.model.VideoType
import io.filmtime.feature.credits.CreditsViewModel

@Composable
fun CreditsRow(
  modifier: Modifier = Modifier,
  tmdbId: Int,
  videoType: VideoType,
) {
  val viewModel = hiltViewModel<CreditsViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  LaunchedEffect(tmdbId, videoType) {
    viewModel.loadCredits(tmdbId, videoType)
  }

  CreditsRow(
    isLoading = state.isLoading,
    credits = state.credit,
    modifier = modifier,
    error = state.error,
    onRetryClick = { viewModel.loadCredits(tmdbId, videoType) },
  )
}

@Composable
internal fun CreditsRow(
  isLoading: Boolean,
  credits: List<Person>,
  modifier: Modifier = Modifier,
  error: UiMessage? = null,
  onRetryClick: () -> Unit,
) {
  Column(
    modifier = modifier,
  ) {
    Text(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 8.dp),
      style = MaterialTheme.typography.titleMedium,
      text = stringResource(R.string.core_ui_cast_crew),
    )
    if (isLoading) {
      CircularProgressIndicator(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentSize(),
      )
    } else if (error != null) {
      ErrorContent(
        uiMessage = error,
        onRetryClick = onRetryClick,
        showGraphicalError = false,
      )
    } else if (credits.isNotEmpty()) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
      ) {
        items(credits) { item ->
          PeopleInCreditsRow(item = item)
        }
      }
    }
  }
}

@ThemePreviews
@Composable
private fun CreditsRowPreview() {
  PreviewFilmTimeTheme {
    CreditsRow(
      isLoading = false,
      credits = listOf(Person.PreviewCast, Person.PreviewCast, Person.PreviewCrew),
      error = null,
      onRetryClick = {},
    )
  }
}
