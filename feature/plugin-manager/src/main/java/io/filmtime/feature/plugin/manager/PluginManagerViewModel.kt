package io.filmtime.feature.plugin.manager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.domain.plugin.GetInstalledPluginsUseCase
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
      }
      .launchIn(viewModelScope)
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
}
