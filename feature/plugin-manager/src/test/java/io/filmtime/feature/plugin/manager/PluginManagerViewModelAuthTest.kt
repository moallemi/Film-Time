package io.filmtime.feature.plugin.manager

import android.content.Intent
import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.data.model.Result
import io.filmtime.domain.testing.FakeCreatePluginLoginIntentUseCase
import io.filmtime.domain.testing.FakeGetInstalledPluginsUseCase
import io.filmtime.domain.testing.FakeGetPluginAuthStateUseCase
import io.filmtime.domain.testing.FakeLogoutPluginUseCase
import io.filmtime.domain.testing.FakeRefreshPluginsUseCase
import io.filmtime.domain.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class PluginManagerViewModelAuthTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var getInstalledPlugins: FakeGetInstalledPluginsUseCase
  private lateinit var refreshPlugins: FakeRefreshPluginsUseCase
  private lateinit var getPluginAuthState: FakeGetPluginAuthStateUseCase
  private lateinit var logoutPlugin: FakeLogoutPluginUseCase
  private lateinit var createPluginLoginIntent: FakeCreatePluginLoginIntentUseCase
  private lateinit var pluginPreferences: FakePluginPreferences

  private val authPlugin = PluginMetadata(
    pluginId = "auth.plugin",
    name = "Auth Plugin",
    description = "Plugin requiring auth",
    version = "1.0.0",
    iconUrl = null,
    authority = "io.filmtime.plugin.auth",
    packageName = "com.test.authplugin",
    requiresAuth = true,
    loginActivityClass = "com.test.authplugin.LoginActivity",
  )

  private val noAuthPlugin = PluginMetadata(
    pluginId = "noauth.plugin",
    name = "No Auth Plugin",
    description = "Plugin without auth",
    version = "1.0.0",
    iconUrl = null,
    authority = "io.filmtime.plugin.noauth",
    packageName = "com.test.noauthplugin",
    requiresAuth = false,
    loginActivityClass = null,
  )

  @Before
  fun setup() {
    getInstalledPlugins = FakeGetInstalledPluginsUseCase()
    refreshPlugins = FakeRefreshPluginsUseCase()
    getPluginAuthState = FakeGetPluginAuthStateUseCase()
    logoutPlugin = FakeLogoutPluginUseCase()
    createPluginLoginIntent = FakeCreatePluginLoginIntentUseCase()
    pluginPreferences = FakePluginPreferences()
  }

  private fun createViewModel(): PluginManagerViewModel {
    return PluginManagerViewModel(
      getInstalledPlugins = getInstalledPlugins,
      refreshPlugins = refreshPlugins,
      getPluginAuthState = getPluginAuthState,
      logoutPluginUseCase = logoutPlugin,
      createPluginLoginIntent = createPluginLoginIntent,
      pluginPreferences = pluginPreferences,
    )
  }

  @Test
  fun `auth states loaded for plugins with requiresAuth`() = runTest {
    getInstalledPlugins.setPlugins(listOf(authPlugin, noAuthPlugin))
    getPluginAuthState.setAuthState(
      authPlugin.pluginId,
      Result.Success(PluginAuthState.Authenticated),
    )

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertEquals(PluginAuthState.Authenticated, state.authStates[authPlugin.pluginId])
    assertNull(state.authStates[noAuthPlugin.pluginId])
  }

  @Test
  fun `auth state not authenticated for unauthenticated plugin`() = runTest {
    getInstalledPlugins.setPlugins(listOf(authPlugin))
    getPluginAuthState.setAuthState(
      authPlugin.pluginId,
      Result.Success(PluginAuthState.NotAuthenticated),
    )

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertEquals(PluginAuthState.NotAuthenticated, state.authStates[authPlugin.pluginId])
  }

  @Test
  fun `loginPlugin sets loginIntent and pendingLoginPluginId`() = runTest {
    getInstalledPlugins.setPlugins(listOf(authPlugin))
    val loginIntent = Intent("io.filmtime.plugin.ACTION_LOGIN")
    createPluginLoginIntent.setIntent(loginIntent)

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.loginPlugin(authPlugin)

    val state = viewModel.state.value
    assertNotNull(state.loginIntent)
    assertEquals(authPlugin.pluginId, state.pendingLoginPluginId)
  }

  @Test
  fun `onLoginResult success refreshes auth state`() = runTest {
    getInstalledPlugins.setPlugins(listOf(authPlugin))
    getPluginAuthState.setAuthState(
      authPlugin.pluginId,
      Result.Success(PluginAuthState.NotAuthenticated),
    )

    val viewModel = createViewModel()
    advanceUntilIdle()

    assertEquals(PluginAuthState.NotAuthenticated, viewModel.state.value.authStates[authPlugin.pluginId])

    createPluginLoginIntent.setIntent(Intent("io.filmtime.plugin.ACTION_LOGIN"))
    viewModel.loginPlugin(authPlugin)

    getPluginAuthState.setAuthState(
      authPlugin.pluginId,
      Result.Success(PluginAuthState.Authenticated),
    )

    viewModel.onLoginResult(true)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.loginIntent)
    assertNull(state.pendingLoginPluginId)
    assertEquals(PluginAuthState.Authenticated, state.authStates[authPlugin.pluginId])
  }

  @Test
  fun `onLoginResult failure clears login state without refresh`() = runTest {
    getInstalledPlugins.setPlugins(listOf(authPlugin))
    getPluginAuthState.setAuthState(
      authPlugin.pluginId,
      Result.Success(PluginAuthState.NotAuthenticated),
    )

    val viewModel = createViewModel()
    advanceUntilIdle()

    createPluginLoginIntent.setIntent(Intent("io.filmtime.plugin.ACTION_LOGIN"))
    viewModel.loginPlugin(authPlugin)

    viewModel.onLoginResult(false)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.loginIntent)
    assertNull(state.pendingLoginPluginId)
    assertEquals(PluginAuthState.NotAuthenticated, state.authStates[authPlugin.pluginId])
  }

  @Test
  fun `logoutPlugin updates auth state to NotAuthenticated`() = runTest {
    getInstalledPlugins.setPlugins(listOf(authPlugin))
    getPluginAuthState.setAuthState(
      authPlugin.pluginId,
      Result.Success(PluginAuthState.Authenticated),
    )

    val viewModel = createViewModel()
    advanceUntilIdle()

    assertEquals(PluginAuthState.Authenticated, viewModel.state.value.authStates[authPlugin.pluginId])

    viewModel.logoutPlugin(authPlugin)
    advanceUntilIdle()

    assertEquals(authPlugin.pluginId, logoutPlugin.lastPluginId)
    assertEquals(PluginAuthState.NotAuthenticated, viewModel.state.value.authStates[authPlugin.pluginId])
  }

  @Test
  fun `loginPlugin with null intent does not update state`() = runTest {
    getInstalledPlugins.setPlugins(listOf(authPlugin))
    createPluginLoginIntent.setIntent(null)

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.loginPlugin(authPlugin)

    val state = viewModel.state.value
    assertNull(state.loginIntent)
    assertNull(state.pendingLoginPluginId)
  }
}

private class FakePluginPreferences : PluginPreferences {
  private var defaultPluginId: String? = null
  override fun getDefaultPluginId(): String? = defaultPluginId
  override fun setDefaultPluginId(pluginId: String?) {
    defaultPluginId = pluginId
  }
}
