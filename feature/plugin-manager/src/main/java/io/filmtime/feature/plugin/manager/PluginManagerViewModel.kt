package io.filmtime.feature.plugin.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.domain.plugin.CreatePluginLoginIntentUseCase
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
import io.filmtime.domain.plugin.GetPluginAuthStateUseCase
import io.filmtime.domain.plugin.LogoutPluginUseCase
import io.filmtime.domain.plugin.RefreshPluginsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PluginManagerViewModel @Inject constructor(
  private val getInstalledPlugins: GetInstalledPluginsUseCase,
  private val refreshPlugins: RefreshPluginsUseCase,
  private val getPluginAuthState: GetPluginAuthStateUseCase,
  private val logoutPluginUseCase: LogoutPluginUseCase,
  private val createPluginLoginIntent: CreatePluginLoginIntentUseCase,
  private val pluginPreferences: PluginPreferences,
) : ViewModel() {

  private val _state = MutableStateFlow(PluginManagerUiState())
  val state = _state.asStateFlow()

  init {
    observePlugins()
    refresh()
  }

  private fun observePlugins() {
    getInstalledPlugins()
      .onEach { plugins ->
        _state.update {
          it.copy(
            isLoading = false,
            plugins = plugins,
            defaultPluginId = pluginPreferences.getDefaultPluginId(),
          )
        }
        loadAuthStates(plugins)
      }
      .launchIn(viewModelScope)
  }

  private fun loadAuthStates(plugins: List<PluginMetadata>) {
    viewModelScope.launch {
      val authStates = mutableMapOf<String, PluginAuthState>()
      plugins.filter { it.requiresAuth }.forEach { plugin ->
        getPluginAuthState(plugin.pluginId).fold(
          onSuccess = { authState -> authStates[plugin.pluginId] = authState },
          onFailure = { authStates[plugin.pluginId] = PluginAuthState.NotAuthenticated },
        )
      }
      _state.update { it.copy(authStates = authStates) }
    }
  }

  fun refresh() {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }
      refreshPlugins()
    }
  }

  fun setDefaultPlugin(pluginId: String?) {
    viewModelScope.launch {
      pluginPreferences.setDefaultPluginId(pluginId)
      _state.update { it.copy(defaultPluginId = pluginId) }
    }
  }

  fun loginPlugin(plugin: PluginMetadata) {
    val intent = createPluginLoginIntent(plugin) ?: return
    _state.update { it.copy(loginIntent = intent, pendingLoginPluginId = plugin.pluginId) }
  }

  fun onLoginResult(success: Boolean) {
    val pluginId = _state.value.pendingLoginPluginId
    _state.update { it.copy(loginIntent = null, pendingLoginPluginId = null) }
    if (success && pluginId != null) {
      viewModelScope.launch {
        getPluginAuthState(pluginId).fold(
          onSuccess = { authState ->
            _state.update { it.copy(authStates = it.authStates + (pluginId to authState)) }
          },
          onFailure = {},
        )
      }
    }
  }

  fun logoutPlugin(plugin: PluginMetadata) {
    viewModelScope.launch {
      logoutPluginUseCase(plugin.pluginId).fold(
        onSuccess = {
          _state.update {
            it.copy(authStates = it.authStates + (plugin.pluginId to PluginAuthState.NotAuthenticated))
          }
        },
        onFailure = {},
      )
    }
  }
}
