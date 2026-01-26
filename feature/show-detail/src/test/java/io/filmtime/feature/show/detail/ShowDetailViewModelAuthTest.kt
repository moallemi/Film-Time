package io.filmtime.feature.show.detail

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import io.filmtime.core.plugin.api.PluginContract
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.plugin.api.PluginStream
import io.filmtime.core.plugin.api.StreamResponse
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.MovieVideo
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktEpisodeHistory
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoType
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.testing.FakeCreatePluginLoginIntentUseCase
import io.filmtime.domain.testing.FakeGetInstalledPluginsUseCase
import io.filmtime.domain.testing.FakeGetStreamFromPluginUseCase
import io.filmtime.domain.testing.FakeRefreshPluginsUseCase
import io.filmtime.domain.testing.util.MainDispatcherRule
import io.filmtime.domain.tmdb.shows.GetEpisodesBySeasonUseCase
import io.filmtime.domain.tmdb.shows.GetShowDetailsUseCase
import io.filmtime.domain.tmdb.shows.GetShowVideosUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
import io.filmtime.domain.trakt.history.AddEpisodeToHistoryUseCase
import io.filmtime.domain.trakt.history.IsShowWatchedUseCase
import io.filmtime.domain.trakt.history.RemoveEpisodeFromHistoryUseCase
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
class ShowDetailViewModelAuthTest {

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

  private val testEpisode = EpisodeThumbnail(
    ids = VideoId(traktId = 1, tmdbId = 1),
    episodeNumber = 1,
    seasonNumber = 1,
    title = "Pilot",
    description = "First episode",
    posterUrl = "",
    airDate = "2024-01-01",
  )

  @Before
  fun setup() {
    getInstalledPlugins = FakeGetInstalledPluginsUseCase()
    refreshPlugins = FakeRefreshPluginsUseCase()
    getStreamFromPlugin = FakeGetStreamFromPluginUseCase()
    createPluginLoginIntent = FakeCreatePluginLoginIntentUseCase()
  }

  private fun createViewModel(): ShowDetailViewModel {
    getInstalledPlugins.setPlugins(listOf(testPlugin))

    return ShowDetailViewModel(
      savedStateHandle = SavedStateHandle(mapOf("video_id" to 1)),
      getShowDetails = FakeGetShowDetailsUseCase(),
      addBookmark = FakeAddBookmarkUseCase(),
      deleteBookmark = FakeDeleteBookmarkUseCase(),
      observeBookmark = FakeObserveBookmarkUseCase(),
      getRatings = FakeGetRatingsUseCase(),
      getEpisodesBySeason = FakeGetEpisodesBySeasonUseCase(),
      isShowWatched = FakeIsShowWatchedUseCase(),
      addToHistory = FakeAddEpisodeToHistoryUseCase(),
      removeFromHistory = FakeRemoveEpisodeFromHistoryUseCase(),
      getShowVideos = FakeGetShowVideosUseCase(),
      getInstalledPlugins = getInstalledPlugins,
      refreshPlugins = refreshPlugins,
      getStreamFromPlugin = getStreamFromPlugin,
      createPluginLoginIntent = createPluginLoginIntent,
      pluginPreferences = FakePluginPreferences(),
    )
  }

  @Test
  fun `auth required error sets pendingAuthPlugin and preserves pendingEpisode`() = runTest {
    getStreamFromPlugin.setResult(Result.Failure(PluginError.AuthenticationRequired))
    val loginIntent = Intent("io.filmtime.plugin.ACTION_LOGIN")
    createPluginLoginIntent.setIntent(loginIntent)

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.playEpisode(testEpisode)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertEquals(testPlugin, state.pendingAuthPlugin)
    assertNotNull(state.loginIntent)
    assertEquals(testEpisode, state.pendingEpisode)
    assertNull(state.streamError)
  }

  @Test
  fun `successful login retries stream with pending episode`() = runTest {
    getStreamFromPlugin.setResult(Result.Failure(PluginError.AuthenticationRequired))

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.playEpisode(testEpisode)
    advanceUntilIdle()

    assertNotNull(viewModel.state.value.pendingAuthPlugin)
    assertNotNull(viewModel.state.value.pendingEpisode)

    val streamResponse = StreamResponse(
      streams = listOf(
        PluginStream(
          url = "https://example.com/episode.m3u8",
          quality = "hd",
          streamType = "hls",
          title = "Episode Stream",
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
    assertNull(state.pendingEpisode)
  }

  @Test
  fun `failed login clears both pendingAuthPlugin and pendingEpisode`() = runTest {
    getStreamFromPlugin.setResult(Result.Failure(PluginError.AuthenticationRequired))

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.playEpisode(testEpisode)
    advanceUntilIdle()

    assertNotNull(viewModel.state.value.pendingAuthPlugin)

    viewModel.onPluginLoginResult(false)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.pendingAuthPlugin)
    assertNull(state.loginIntent)
    assertNull(state.pendingEpisode)
  }

  @Test
  fun `non-auth error sets streamError and clears pendingEpisode`() = runTest {
    getStreamFromPlugin.setResult(
      Result.Failure(PluginError.CommunicationError("Connection failed")),
    )

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.playEpisode(testEpisode)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.pendingAuthPlugin)
    assertNull(state.loginIntent)
    assertNotNull(state.streamError)
    assertNull(state.pendingEpisode)
  }

  @Test
  fun `embed stream type is preserved for episode`() = runTest {
    val streamResponse = StreamResponse(
      streams = listOf(
        PluginStream(
          url = "https://example.com/embed/tv/123/1/1",
          quality = "auto",
          streamType = PluginContract.StreamType.EMBED,
          title = "Watch Episode",
          headers = emptyMap(),
          subtitles = emptyList(),
        ),
      ),
    )
    getStreamFromPlugin.setResult(Result.Success(streamResponse))

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.navigateToPlayer.test {
      viewModel.playEpisode(testEpisode)
      advanceUntilIdle()

      val streamInfo = awaitItem()
      assertNotNull(streamInfo)
      assertEquals(PluginContract.StreamType.EMBED, streamInfo?.streamType)
      assertEquals("https://example.com/embed/tv/123/1/1", streamInfo?.url)
    }
  }

  @Test
  fun `hls stream type is preserved for episode`() = runTest {
    val streamResponse = StreamResponse(
      streams = listOf(
        PluginStream(
          url = "https://example.com/episode.m3u8",
          quality = "hd",
          streamType = PluginContract.StreamType.HLS,
          title = "Episode Stream",
          headers = mapOf("User-Agent" to "TestAgent"),
          subtitles = emptyList(),
        ),
      ),
    )
    getStreamFromPlugin.setResult(Result.Success(streamResponse))

    val viewModel = createViewModel()
    advanceUntilIdle()

    viewModel.navigateToPlayer.test {
      viewModel.playEpisode(testEpisode)
      advanceUntilIdle()

      val streamInfo = awaitItem()
      assertNotNull(streamInfo)
      assertEquals(PluginContract.StreamType.HLS, streamInfo?.streamType)
      assertEquals("https://example.com/episode.m3u8", streamInfo?.url)
    }
  }
}

private val testVideoDetail = VideoDetail(
  ids = VideoId(traktId = 1, tmdbId = 1),
  title = "Test Show",
  posterUrl = "",
  coverUrl = "",
  year = 2024,
  genres = emptyList(),
  originalLanguage = "en",
  spokenLanguages = emptyList(),
  description = "A test show",
  runtime = "45min",
  releaseDate = "2024-01-01",
  tagline = null,
  budget = null,
  homePage = null,
  status = null,
  networks = null,
  seasonsNumber = 2,
  collectionId = null,
)

private class FakeGetShowDetailsUseCase : GetShowDetailsUseCase {
  override suspend fun invoke(showId: Int): Result<VideoDetail, GeneralError> =
    Result.Success(testVideoDetail)
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

private class FakeGetEpisodesBySeasonUseCase : GetEpisodesBySeasonUseCase {
  override suspend fun invoke(
    showId: Int,
    seasonNumber: Int,
  ): Result<List<EpisodeThumbnail>, GeneralError> = Result.Success(emptyList())
}

private class FakeIsShowWatchedUseCase : IsShowWatchedUseCase {
  override suspend fun invoke(
    tmdbId: Int,
    seasonNumber: Int,
  ): Result<Map<Int, List<TraktEpisodeHistory>>, GeneralError> = Result.Success(emptyMap())
}

private class FakeAddEpisodeToHistoryUseCase : AddEpisodeToHistoryUseCase {
  override suspend fun invoke(
    tmdbId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
  ): Result<Unit, GeneralError> = Result.Success(Unit)
}

private class FakeRemoveEpisodeFromHistoryUseCase : RemoveEpisodeFromHistoryUseCase {
  override suspend fun invoke(
    tmdbId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
  ): Result<Unit, GeneralError> = Result.Success(Unit)
}

private class FakeGetShowVideosUseCase : GetShowVideosUseCase {
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
