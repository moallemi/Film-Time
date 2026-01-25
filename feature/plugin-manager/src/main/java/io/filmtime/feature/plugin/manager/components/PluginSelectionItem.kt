package io.filmtime.feature.plugin.manager.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.plugin.api.PluginMetadata

@Composable
internal fun PluginSelectionItem(
  plugin: PluginMetadata,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(modifier = Modifier.width(8.dp))
    Icon(
      modifier = Modifier
        .align(Alignment.Top),
      imageVector = Icons.Default.Extension,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Column(
      modifier = Modifier.weight(1f),
    ) {
      Text(
        text = plugin.name,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      )
      Text(
        text = plugin.description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

@ThemePreviews
@Composable
private fun PluginSelectionItemPreview() {
  PreviewFilmTimeTheme {
    PluginSelectionItem(
      plugin = PluginMetadata(
        pluginId = "plugin-id",
        name = "Test Plugin",
        description = "This is a test plugin with a long description that should wrap " +
          "properly and do not show ellipsis",
        version = "1.0.0",
        iconUrl = null,
        authority = "io.filmtime.test.plugin",
      ),
      isSelected = false,
      onClick = {},
    )
  }
}

@ThemePreviews
@Composable
private fun PluginSelectionItemSelectedPreview() {
  PreviewFilmTimeTheme {
    PluginSelectionItem(
      plugin = PluginMetadata(
        pluginId = "plugin-id",
        name = "Test Plugin",
        description = "This is a test plugin",
        version = "1.0.0",
        iconUrl = null,
        authority = "io.filmtime.test.plugin",
      ),
      isSelected = true,
      onClick = {},
    )
  }
}
