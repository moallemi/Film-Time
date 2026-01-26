package io.filmtime.feature.plugin.manager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.designsystem.composable.FilmTimeCircularProgressBar
import io.filmtime.core.plugin.api.PluginContract
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.feature.plugin.manager.components.EmptyPluginsMessage
import io.filmtime.feature.plugin.manager.components.PluginCard

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

  LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
    viewModel.refresh()
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
    if (state.isLoading) {
      item {
        FilmTimeCircularProgressBar(
          modifier = Modifier
            .animateItem()
            .fillParentMaxSize()
            .wrapContentSize(),
        )
      }
    } else if (state.plugins.isEmpty()) {
      item {
        EmptyPluginsMessage(
          modifier = Modifier
            .animateItem()
            .fillParentMaxSize()
            .wrapContentSize(),
        )
      }
    } else {
      items(state.plugins, key = { it.pluginId }) { plugin ->
        PluginCard(
          modifier = Modifier
            .animateItem(),
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
      item {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = stringResource(R.string.plugin_manager_default_hint),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}
