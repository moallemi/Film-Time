package io.filmtime.feature.plugin.manager.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.feature.plugin.manager.R

@Composable
internal fun PluginCard(
  plugin: PluginMetadata,
  isDefault: Boolean,
  authState: PluginAuthState?,
  onClick: () -> Unit,
  onLoginClick: () -> Unit,
  onLogoutClick: () -> Unit,
  onViewInfoClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var menuExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp)
        .padding(vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        modifier = Modifier
          .padding(top = 4.dp)
          .align(Alignment.Top),
        imageVector = Icons.Default.Extension,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
      )
      Spacer(modifier = Modifier.width(16.dp))
      Column(
        modifier = Modifier.weight(1f),
      ) {
        Text(
          text = plugin.name,
          style = MaterialTheme.typography.titleMedium,
        )
        Text(
          text = plugin.description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
          text = stringResource(R.string.plugin_manager_version, plugin.version),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (plugin.requiresAuth && authState != null) {
          Spacer(modifier = Modifier.height(8.dp))
          when (authState) {
            is PluginAuthState.Authenticated -> {
              OutlinedButton(onClick = onLogoutClick) {
                Text(stringResource(R.string.plugin_manager_logout))
              }
            }
            is PluginAuthState.NotAuthenticated -> {
              FilledTonalButton(onClick = onLoginClick) {
                Text(stringResource(R.string.plugin_manager_login))
              }
            }
            else -> {}
          }
        }
      }
      if (isDefault) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = stringResource(R.string.plugin_manager_default),
          tint = MaterialTheme.colorScheme.primary,
        )
      }
      Box {
        IconButton(onClick = { menuExpanded = true }) {
          Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.plugin_manager_menu),
          )
        }
        DropdownMenu(
          expanded = menuExpanded,
          onDismissRequest = { menuExpanded = false },
        ) {
          DropdownMenuItem(
            text = {
              Text(
                stringResource(
                  if (isDefault) {
                    R.string.plugin_manager_remove_default
                  } else {
                    R.string.plugin_manager_set_default
                  },
                ),
              )
            },
            onClick = {
              menuExpanded = false
              onClick()
            },
          )
          DropdownMenuItem(
            text = { Text(stringResource(R.string.plugin_manager_view_info)) },
            onClick = {
              menuExpanded = false
              onViewInfoClick()
            },
          )
        }
      }
    }
  }
}

@ThemePreviews
@Composable
private fun PluginCardPreview() {
  PreviewFilmTimeTheme {
    PluginCard(
      plugin = PluginMetadata(
        pluginId = "plugin-id",
        name = "Test Plugin",
        description = "This is a test plugin",
        version = "1.0.0",
        iconUrl = null,
        authority = "io.filmtime.test.plugin",
      ),
      isDefault = false,
      authState = null,
      onClick = {},
      onLoginClick = {},
      onLogoutClick = {},
      onViewInfoClick = {},
    )
  }
}

@ThemePreviews
@Composable
private fun PluginCardDefaultPreview() {
  PreviewFilmTimeTheme {
    PluginCard(
      plugin = PluginMetadata(
        pluginId = "plugin-id",
        name = "Test Plugin",
        description = "This is a test plugin",
        version = "1.0.0",
        iconUrl = null,
        authority = "io.filmtime.test.plugin",
      ),
      isDefault = true,
      authState = null,
      onClick = {},
      onLoginClick = {},
      onLogoutClick = {},
      onViewInfoClick = {},
    )
  }
}

@ThemePreviews
@Composable
private fun PluginCardWithAuthPreview() {
  PreviewFilmTimeTheme {
    PluginCard(
      plugin = PluginMetadata(
        pluginId = "plugin-id",
        name = "Test Plugin",
        description = "This is a test plugin that requires authentication",
        version = "1.0.0",
        iconUrl = null,
        authority = "io.filmtime.test.plugin",
        requiresAuth = true,
      ),
      isDefault = false,
      authState = PluginAuthState.NotAuthenticated,
      onClick = {},
      onLoginClick = {},
      onLogoutClick = {},
      onViewInfoClick = {},
    )
  }
}
