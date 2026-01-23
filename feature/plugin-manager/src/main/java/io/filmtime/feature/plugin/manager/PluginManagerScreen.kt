package io.filmtime.feature.plugin.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.plugin.api.PluginMetadata

@Composable
fun PluginManagerScreen(
  onBackClick: () -> Unit,
) {
  val viewModel = hiltViewModel<PluginManagerViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()

  PluginManagerScreen(
    state = state,
    onBackClick = onBackClick,
    onPluginClick = { plugin ->
      val newDefault = if (state.defaultPluginId == plugin.pluginId) null else plugin.pluginId
      viewModel.setDefaultPlugin(newDefault)
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PluginManagerScreen(
  state: PluginManagerUiState,
  onBackClick: () -> Unit,
  onPluginClick: (PluginMetadata) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(stringResource(R.string.plugin_manager_title)) },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = stringResource(R.string.plugin_manager_back),
            )
          }
        },
      )
    },
  ) { padding ->
    PluginManagerContent(
      state = state,
      contentPadding = padding,
      onPluginClick = onPluginClick,
    )
  }
}

@Composable
private fun PluginManagerContent(
  state: PluginManagerUiState,
  contentPadding: PaddingValues,
  onPluginClick: (PluginMetadata) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(contentPadding),
    contentPadding = PaddingValues(16.dp),
  ) {
    if (state.plugins.isEmpty() && !state.isLoading) {
      item {
        EmptyPluginsMessage()
      }
    } else {
      items(state.plugins, key = { it.pluginId }) { plugin ->
        PluginCard(
          plugin = plugin,
          isDefault = plugin.pluginId == state.defaultPluginId,
          onClick = { onPluginClick(plugin) },
        )
        Spacer(modifier = Modifier.height(8.dp))
      }
    }
  }
}

@Composable
private fun PluginCard(
  plugin: PluginMetadata,
  isDefault: Boolean,
  onClick: () -> Unit,
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick),
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
      }
      if (isDefault) {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = stringResource(R.string.plugin_manager_default),
          tint = MaterialTheme.colorScheme.primary,
        )
      }
    }
  }
}

@Composable
private fun EmptyPluginsMessage() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      imageVector = Icons.Default.Extension,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = stringResource(R.string.plugin_manager_no_plugins_title),
      style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = stringResource(R.string.plugin_manager_no_plugins_description),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
