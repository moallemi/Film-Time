package io.filmtime.feature.plugin.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.domain.plugin.CreatePluginLoginIntentUseCase
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
import io.filmtime.domain.plugin.GetPluginAuthStateUseCase
import io.filmtime.domain.plugin.LogoutPluginUseCase
import io.filmtime.domain.plugin.RefreshPluginsUseCase
import io.filmtime.feature.plugin.manager.PluginManagerAction.LoginPlugin
import io.filmtime.feature.plugin.manager.PluginManagerAction.LoginResult
import io.filmtime.feature.plugin.manager.PluginManagerAction.LogoutPlugin
import io.filmtime.feature.plugin.manager.PluginManagerAction.Refresh
import io.filmtime.feature.plugin.manager.PluginManagerAction.SetDefaultPlugin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class PluginManagerViewModel @Inject constructor(
  private val getInstalledPlugins: GetInstalledPluginsUseCase,
  private val refreshPlugins: RefreshPluginsUseCase,
  private val getPluginAuthState: GetPluginAuthStateUseCase,
  private val logoutPluginUseCase: LogoutPluginUseCase,
  private val createPluginLoginIntent: CreatePluginLoginIntentUseCase,
  private val pluginPreferences: PluginPreferences,
) : ViewModel() {

  private val pendingActions = MutableSharedFlow<PluginManagerAction>()

  private val _state = MutableStateFlow(PluginManagerUiState())
  val state = _state.asStateFlow()

  init {
    collectActions()
    observePlugins()
    refresh()
  }

  fun submitAction(action: PluginManagerAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is Refresh -> refresh()
        is SetDefaultPlugin -> setDefaultPlugin(action.pluginId)
        is LoginPlugin -> loginPlugin(action.plugin)
        is LoginResult -> onLoginResult(action.success)
        is LogoutPlugin -> logoutPlugin(action.plugin)
      }
    }
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
    launch {
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

  private fun refresh() {
    launch {
      _state.update { it.copy(isLoading = true) }
      refreshPlugins()
      _state.update { it.copy(isLoading = false) }
    }
  }

  private fun setDefaultPlugin(pluginId: String?) {
    launch {
      pluginPreferences.setDefaultPluginId(pluginId)
      _state.update { it.copy(defaultPluginId = pluginId) }
    }
  }

  private fun loginPlugin(plugin: PluginMetadata) {
    val intent = createPluginLoginIntent(plugin) ?: return
    _state.update { it.copy(loginIntent = intent, pendingLoginPluginId = plugin.pluginId) }
  }

  private fun onLoginResult(success: Boolean) {
    val pluginId = _state.value.pendingLoginPluginId
    _state.update { it.copy(loginIntent = null, pendingLoginPluginId = null) }
    if (success && pluginId != null) {
      launch {
        getPluginAuthState(pluginId).fold(
          onSuccess = { authState ->
            _state.update { it.copy(authStates = it.authStates + (pluginId to authState)) }
          },
          onFailure = {},
        )
      }
    }
  }

  private fun logoutPlugin(plugin: PluginMetadata) {
    launch {
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
