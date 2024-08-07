package io.filmtime.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.designsystem.composable.FilmTimeSmallTopAppBar
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.feature.settings.components.FilmTimeCard
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
    onTraktLogoutClick = viewModel::traktLogout,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
  state: SettingsUiState,
  onTraktLoginClick: () -> Unit,
  onTraktLogoutClick: () -> Unit,
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
      onTraktLogoutClick = onTraktLogoutClick,
    )
  }
}

@Composable
private fun SettingsContent(
  state: SettingsUiState,
  contentPadding: PaddingValues,
  onTraktLoginClick: () -> Unit,
  onTraktLogoutClick: () -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(contentPadding),
  ) {
    LazyColumn {
      item {
        TraktCard(
          modifier = Modifier
            .padding(16.dp),
          isLoggedIn = state.isTraktLoggedIn,
          onLoginClick = onTraktLoginClick,
          onLogoutClick = onTraktLogoutClick,
        )
      }
    }
    FilmTimeCard(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(16.dp),
    )
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
      onTraktLogoutClick = { },
    )
  }
}
