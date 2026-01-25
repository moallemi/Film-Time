package io.filmtime.feature.plugin.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.plugin.api.PluginMetadata

@Composable
fun PluginSelectionDialog(
  plugins: List<PluginMetadata>,
  selectedPluginId: String?,
  onPluginSelected: (PluginMetadata) -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(stringResource(R.string.plugin_selection_title))
    },
    text = {
      Column {
        LazyColumn(
          modifier = Modifier.weight(1f, fill = false),
        ) {
          items(plugins, key = { it.pluginId }) { plugin ->
            PluginSelectionItem(
              plugin = plugin,
              isSelected = plugin.pluginId == selectedPluginId,
              onClick = { onPluginSelected(plugin) },
            )
          }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = stringResource(R.string.plugin_selection_default_hint),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(R.string.plugin_selection_cancel))
      }
    },
  )
}

@Composable
private fun PluginSelectionItem(
  plugin: PluginMetadata,
  isSelected: Boolean,
  onClick: () -> Unit,
) {
  Row(
    modifier = Modifier
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

@Composable
fun NoPluginsInstalledDialog(
  onDismiss: () -> Unit,
  onOpenPluginManager: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(stringResource(R.string.no_plugins_dialog_title))
    },
    text = {
      Text(stringResource(R.string.no_plugins_dialog_message))
    },
    confirmButton = {
      TextButton(onClick = onOpenPluginManager) {
        Text(stringResource(R.string.no_plugins_dialog_open_manager))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text(stringResource(R.string.no_plugins_dialog_cancel))
      }
    },
  )
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
