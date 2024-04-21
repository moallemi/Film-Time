package io.filmtime.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.composable.FilmTimeFilledButton
import io.filmtime.core.designsystem.composable.FilmTimeFilledTonalButton
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.feature.settings.R

@Composable
internal fun TraktCard(
  isLoggedIn: Boolean,
  modifier: Modifier = Modifier,
  onLoginClick: () -> Unit,
) {
  ElevatedCard(
    modifier = modifier,
  ) {
    Image(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .padding(horizontal = 64.dp)
        .padding(vertical = 16.dp),
      painter = painterResource(id = R.drawable.ic_trakt_wide_red),
      contentDescription = stringResource(R.string.feature_settings_trakt_logo),
      contentScale = ContentScale.Inside,
    )
    if (isLoggedIn) {
      FilmTimeFilledTonalButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        onClick = { },
      ) {
        Text(text = stringResource(R.string.feature_settings_sign_out))
      }
    } else {
      FilmTimeFilledButton(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        onClick = onLoginClick,
      ) {
        Text(text = stringResource(R.string.feature_settings_sign_in))
      }
    }
    Text(
      modifier = Modifier
        .padding(16.dp),
      text = stringResource(R.string.feature_settings_trackt_sign_in_desc),
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}

@ThemePreviews
@Composable
private fun SettingsScreenPreview() {
  PreviewFilmTimeTheme {
    Column(
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      TraktCard(
        isLoggedIn = false,
        onLoginClick = {},
      )

      TraktCard(
        isLoggedIn = true,
        onLoginClick = {},
      )
    }
  }
}
