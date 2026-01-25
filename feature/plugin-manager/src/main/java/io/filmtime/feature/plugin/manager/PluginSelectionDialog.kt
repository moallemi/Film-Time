package io.filmtime.feature.plugin.manager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.feature.plugin.manager.components.PluginSelectionItem

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
