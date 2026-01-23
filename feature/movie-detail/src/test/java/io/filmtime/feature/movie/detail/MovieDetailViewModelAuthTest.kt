package io.filmtime.feature.movie.detail

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.plugin.api.PluginStream
import io.filmtime.core.plugin.api.StreamResponse
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.MovieVideo
import io.filmtime.data.model.PreviewMovie
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoType
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.testing.FakeCreatePluginLoginIntentUseCase
import io.filmtime.domain.testing.FakeGetInstalledPluginsUseCase
import io.filmtime.domain.testing.FakeGetStreamFromPluginUseCase
import io.filmtime.domain.testing.FakeRefreshPluginsUseCase
import io.filmtime.domain.testing.util.MainDispatcherRule
import io.filmtime.domain.tmdb.movies.GetMovieCollectionUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.tmdb.movies.GetMovieVideosUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
import io.filmtime.feature.plugin.manager.PluginPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelAuthTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var getInstalledPlugins: FakeGetInstalledPluginsUseCase
  private lateinit var refreshPlugins: FakeRefreshPluginsUseCase
  private lateinit var getStreamFromPlugin: FakeGetStreamFromPluginUseCase
  private lateinit var createPluginLoginIntent: FakeCreatePluginLoginIntentUseCase

  private val testPlugin = PluginMetadata(
    pluginId = "test.plugin",
    name = "Test Plugin",
    description = "A test plugin",
    version = "1.0.0",
    iconUrl = null,
    authority = "io.filmtime.plugin.test",
    packageName = "com.test.plugin",
    requiresAuth = true,
    loginActivityClass = "com.test.plugin.LoginActivity",
  )

  @Before
  fun setup() {
    getInstalledPlugins = FakeGetInstalledPluginsUseCase()
    refreshPlugins = FakeRefreshPluginsUseCase()
    getStreamFromPlugin = FakeGetStreamFromPluginUseCase()
    createPluginLoginIntent = FakeCreatePluginLoginIntentUseCase()
  }

  private fun createViewModel(): MovieDetailViewModel {
    getInstalledPlugins.setPlugins(listOf(testPlugin))

    return MovieDetailViewModel(
      savedStateHandle = SavedStateHandle(mapOf("video_id" to 1)),
      getMovieDetail = FakeGetMovieDetailsUseCase(),
      addBookmark = FakeAddBookmarkUseCase(),
      deleteBookmark = FakeDeleteBookmarkUseCase(),
      observeBookmark = FakeObserveBookmarkUseCase(),
      getRatings = FakeGetRatingsUseCase(),
      getCollection = FakeGetMovieCollectionUseCase(),
      getMovieVideos = FakeGetMovieVideosUseCase(),
      getInstalledPlugins = getInstalledPlugins,
      refreshPlugins = refreshPlugins,
      getStreamFromPlugin = getStreamFromPlugin,
      createPluginLoginIntent = createPluginLoginIntent,
      pluginPreferences = FakePluginPreferences(),
    )
  }

  @Test
  fun `auth required error sets pendingAuthPlugin and loginIntent`() = runTest {
    getStreamFromPlugin.setResult(Result.Failure(PluginError.AuthenticationRequired))
    val loginIntent = Intent("io.filmtime.plugin.ACTION_LOGIN")
    createPluginLoginIntent.setIntent(loginIntent)

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.loadStreamInfo()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertEquals(testPlugin, state.pendingAuthPlugin)
    assertNotNull(state.loginIntent)
    assertNull(state.streamError)
  }

  @Test
  fun `successful login retries stream and navigates to player`() = runTest {
    getStreamFromPlugin.setResult(Result.Failure(PluginError.AuthenticationRequired))

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.loadStreamInfo()
    advanceUntilIdle()

    assertNotNull(viewModel.state.value.pendingAuthPlugin)

    val streamResponse = StreamResponse(
      streams = listOf(
        PluginStream(
          url = "https://example.com/stream.m3u8",
          quality = "hd",
          streamType = "hls",
          title = "Test Stream",
          headers = emptyMap(),
          subtitles = emptyList(),
        ),
      ),
    )
    getStreamFromPlugin.setResult(Result.Success(streamResponse))

    viewModel.onPluginLoginResult(true)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.pendingAuthPlugin)
    assertNull(state.loginIntent)
    assertNotNull(state.streamInfo)
    assertEquals("https://example.com/stream.m3u8", state.streamInfo?.url)
  }

  @Test
  fun `failed login clears pending state without retry`() = runTest {
    getStreamFromPlugin.setResult(Result.Failure(PluginError.AuthenticationRequired))

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.loadStreamInfo()
    advanceUntilIdle()

    assertNotNull(viewModel.state.value.pendingAuthPlugin)

    viewModel.onPluginLoginResult(false)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.pendingAuthPlugin)
    assertNull(state.loginIntent)
    assertNull(state.streamInfo)
  }

  @Test
  fun `non-auth error sets streamError instead of login flow`() = runTest {
    getStreamFromPlugin.setResult(
      Result.Failure(PluginError.CommunicationError("Connection failed")),
    )

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.loadStreamInfo()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.pendingAuthPlugin)
    assertNull(state.loginIntent)
    assertNotNull(state.streamError)
  }

  @Test
  fun `createPluginLoginIntent is called with correct plugin`() = runTest {
    getStreamFromPlugin.setResult(Result.Failure(PluginError.AuthenticationRequired))

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.loadStreamInfo()
    advanceUntilIdle()

    assertEquals(testPlugin, createPluginLoginIntent.lastPlugin)
  }
}

private class FakeGetMovieDetailsUseCase : GetMovieDetailsUseCase {
  override suspend fun invoke(movieId: Int): Flow<Result<VideoDetail, GeneralError>> =
    flowOf(Result.Success(VideoDetail.PreviewMovie))
}

private class FakeAddBookmarkUseCase : AddBookmarkUseCase {
  override suspend fun invoke(tmdbId: Int, type: VideoType) {}
}

private class FakeDeleteBookmarkUseCase : DeleteBookmarkUseCase {
  override suspend fun invoke(tmdbId: Int, type: VideoType) {}
}

private class FakeObserveBookmarkUseCase : ObserveBookmarkUseCase {
  override suspend fun invoke(tmdbId: Int, type: VideoType): Flow<Boolean> = flowOf(false)
}

private class FakeGetRatingsUseCase : GetRatingsUseCase {
  override suspend fun invoke(type: VideoType, tmdbId: Int): Result<Ratings, GeneralError> =
    Result.Success(Ratings())
}

private class FakeGetMovieCollectionUseCase : GetMovieCollectionUseCase {
  override suspend fun invoke(collectionId: Int): Result<MovieCollection, GeneralError> =
    Result.Failure(GeneralError.NetworkError)
}

private class FakeGetMovieVideosUseCase : GetMovieVideosUseCase {
  override suspend fun invoke(movieId: Int): Result<List<MovieVideo>, GeneralError> =
    Result.Success(emptyList())
}

private class FakePluginPreferences : PluginPreferences {
  private var defaultPluginId: String? = null
  override fun getDefaultPluginId(): String? = defaultPluginId
  override fun setDefaultPluginId(pluginId: String?) {
    defaultPluginId = pluginId
  }
}
