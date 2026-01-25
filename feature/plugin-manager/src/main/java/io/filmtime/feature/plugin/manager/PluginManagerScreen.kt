package io.filmtime.feature.plugin.manager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginContract
import io.filmtime.core.plugin.api.PluginMetadata

@Composable
fun PluginManagerScreen(
  onBackClick: () -> Unit,
) {
  val viewModel = hiltViewModel<PluginManagerViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()
  val context = LocalContext.current

  val loginLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
  ) { result ->
    val loginResult = result.data?.getIntExtra(
      PluginContract.Auth.EXTRA_LOGIN_RESULT,
      PluginContract.Auth.LOGIN_RESULT_CANCELLED,
    ) ?: PluginContract.Auth.LOGIN_RESULT_CANCELLED
    val success = result.resultCode == Activity.RESULT_OK &&
      loginResult == PluginContract.Auth.LOGIN_RESULT_SUCCESS
    viewModel.onLoginResult(success)
  }

  LaunchedEffect(state.loginIntent) {
    state.loginIntent?.let { intent ->
      loginLauncher.launch(intent)
    }
  }

  PluginManagerScreen(
    state = state,
    onBackClick = onBackClick,
    onPluginClick = { plugin ->
      val newDefault = if (state.defaultPluginId == plugin.pluginId) null else plugin.pluginId
      viewModel.setDefaultPlugin(newDefault)
    },
    onLoginClick = viewModel::loginPlugin,
    onLogoutClick = viewModel::logoutPlugin,
    onViewInfoClick = { plugin ->
      val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.parse("package:${plugin.packageName}")
      }
      context.startActivity(intent)
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PluginManagerScreen(
  state: PluginManagerUiState,
  onBackClick: () -> Unit,
  onPluginClick: (PluginMetadata) -> Unit,
  onLoginClick: (PluginMetadata) -> Unit,
  onLogoutClick: (PluginMetadata) -> Unit,
  onViewInfoClick: (PluginMetadata) -> Unit,
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
      onLoginClick = onLoginClick,
      onLogoutClick = onLogoutClick,
      onViewInfoClick = onViewInfoClick,
    )
  }
}

@Composable
private fun PluginManagerContent(
  state: PluginManagerUiState,
  contentPadding: PaddingValues,
  onPluginClick: (PluginMetadata) -> Unit,
  onLoginClick: (PluginMetadata) -> Unit,
  onLogoutClick: (PluginMetadata) -> Unit,
  onViewInfoClick: (PluginMetadata) -> Unit,
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
          authState = state.authStates[plugin.pluginId],
          onClick = { onPluginClick(plugin) },
          onLoginClick = { onLoginClick(plugin) },
          onLogoutClick = { onLogoutClick(plugin) },
          onViewInfoClick = { onViewInfoClick(plugin) },
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
  authState: PluginAuthState?,
  onClick: () -> Unit,
  onLoginClick: () -> Unit,
  onLogoutClick: () -> Unit,
  onViewInfoClick: () -> Unit,
) {
  var menuExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
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
                  if (isDefault) R.string.plugin_manager_remove_default
                  else R.string.plugin_manager_set_default,
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
