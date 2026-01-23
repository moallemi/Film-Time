package io.filmtime.feature.plugin.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
      LazyColumn {
        items(plugins, key = { it.pluginId }) { plugin ->
          PluginSelectionItem(
            plugin = plugin,
            isSelected = plugin.pluginId == selectedPluginId,
            onClick = { onPluginSelected(plugin) },
          )
        }
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
    RadioButton(
      selected = isSelected,
      onClick = onClick,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Icon(
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
