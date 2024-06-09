package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.R
import io.filmtime.data.model.Person
import io.filmtime.data.model.PreviewCast
import io.filmtime.data.model.PreviewCrew

@Composable
fun CreditsRow(
  isLoading: Boolean,
  credits: List<Person>,
  modifier: Modifier = Modifier,
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
      CircularProgressIndicator()
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
    )
  }
}
