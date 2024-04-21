package io.filmtime.feature.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.designsystem.composable.FilmTimeSmallTopAppBar
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.feature.settings.components.TraktCard

@Composable
fun SettingsScreen(
  onTraktLoginClick: () -> Unit,
) {
  val viewModel = hiltViewModel<SettingsViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  SettingsScreen(
    state = state,
    onTraktLoginClick = onTraktLoginClick,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
  state: SettingsUiState,
  onTraktLoginClick: () -> Unit,
) {
  Scaffold(
    topBar = {
      FilmTimeSmallTopAppBar(
        title = stringResource(R.string.feature_settings_settings),
      )
    },
  ) { padding ->
    SettingsContent(
      state = state,
      contentPadding = padding,
      onTraktLoginClick = onTraktLoginClick,
    )
  }
}

@Composable
private fun SettingsContent(
  state: SettingsUiState,
  contentPadding: PaddingValues,
  onTraktLoginClick: () -> Unit,
) {
  LazyColumn(
    contentPadding = contentPadding,
  ) {
    item {
      TraktCard(
        modifier = Modifier
          .padding(16.dp),
        isLoggedIn = state.isTraktLoggedIn,
        onLoginClick = onTraktLoginClick,
      )
    }
  }
}

@ThemePreviews
@Composable
private fun SettingsScreenPreview() {
  PreviewFilmTimeTheme {
    SettingsContent(
      state = SettingsUiState(),
      contentPadding = PaddingValues(16.dp),
      onTraktLoginClick = { },
    )
  }
}
