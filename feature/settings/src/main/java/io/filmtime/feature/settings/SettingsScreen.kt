package io.filmtime.feature.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.composable.FilmTimeSmallTopAppBar
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.feature.settings.components.TraktCard

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
  SettingsScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen() {
  Scaffold(
    topBar = {
      FilmTimeSmallTopAppBar(
        title = stringResource(R.string.feature_settings_settings),
      )
    },
  ) { padding ->
    SettingsContent(
      contentPadding = padding,
    )
  }
}

@Composable
private fun SettingsContent(
  contentPadding: PaddingValues,
) {
  LazyColumn(
    contentPadding = contentPadding,
  ) {
    item {
      TraktCard(
        modifier = Modifier
          .padding(16.dp),
      )
    }
  }
}

@ThemePreviews
@Composable
private fun SettingsScreenPreview() {
  PreviewFilmTimeTheme {
    SettingsContent(
      contentPadding = PaddingValues(16.dp),
    )
  }
}
