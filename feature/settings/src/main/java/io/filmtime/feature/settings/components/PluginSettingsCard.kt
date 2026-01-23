package io.filmtime.feature.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.composable.FilmTimeFilledButton
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.feature.settings.R

@Composable
internal fun PluginSettingsCard(
  modifier: Modifier = Modifier,
  onManagePluginsClick: () -> Unit,
) {
  ElevatedCard(
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        imageVector = Icons.Default.Extension,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
      )
      Spacer(modifier = Modifier.width(16.dp))
      Column(
        modifier = Modifier.weight(1f),
      ) {
        Text(
          text = stringResource(R.string.feature_settings_stream_providers),
          style = MaterialTheme.typography.titleMedium,
        )
        Text(
          text = stringResource(R.string.feature_settings_stream_providers_desc),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    FilmTimeFilledButton(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .padding(bottom = 16.dp),
      onClick = onManagePluginsClick,
    ) {
      Text(text = stringResource(R.string.feature_settings_manage_providers))
    }
  }
}

@ThemePreviews
@Composable
private fun PluginSettingsCardPreview() {
  PreviewFilmTimeTheme {
    PluginSettingsCard(
      onManagePluginsClick = {},
    )
  }
}
