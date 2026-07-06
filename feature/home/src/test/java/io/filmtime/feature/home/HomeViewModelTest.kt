package io.filmtime.feature.home

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoListType
import io.filmtime.domain.testing.FakeGetBookmarkedMoviesUseCase
import io.filmtime.domain.testing.FakeGetBookmarkedShowsUseCase
import io.filmtime.domain.testing.FakeGetMoviesListUseCase
import io.filmtime.domain.testing.FakeGetTrendingShowsUseCase
import io.filmtime.domain.testing.util.MainDispatcherRule
import io.filmtime.domain.testing.util.TestDataFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var getMoviesList: FakeGetMoviesListUseCase
  private lateinit var getTrendingShows: FakeGetTrendingShowsUseCase
  private lateinit var getBookmarkedMovies: FakeGetBookmarkedMoviesUseCase
  private lateinit var getBookmarkedShows: FakeGetBookmarkedShowsUseCase

  @Before
  fun setup() {
    getMoviesList = FakeGetMoviesListUseCase()
    getTrendingShows = FakeGetTrendingShowsUseCase()
    getBookmarkedMovies = FakeGetBookmarkedMoviesUseCase()
    getBookmarkedShows = FakeGetBookmarkedShowsUseCase()
  }

  @Test
  fun `initial state should have isLoading true and empty sections`() = runTest {
    val viewModel = createViewModel()

    // Check state before any coroutines run
    val initialState = viewModel.state.value
    assertTrue(initialState.isLoading)
    assertTrue(initialState.videoSections.isEmpty())
    assertNull(initialState.error)
    assertNull(initialState.bookmarkedMovies)
    assertNull(initialState.bookmarkedShows)
  }

  @Test
  fun `trending movies success should add movies section and set loading false`() = runTest {
    val movieCount = 3
    val movies = TestDataFactory.createMovies(movieCount)
    getMoviesList.setResult(VideoListType.Trending, Result.Success(movies))

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    val moviesSection = state.videoSections.find { it.type == SectionType.TrendingMovies }
    assertFalse(state.isLoading)
    assertNull(state.error)
    assertNotNull(moviesSection)
    assertEquals(SectionType.TrendingMovies.title, moviesSection.title)
    assertEquals(movieCount, moviesSection.items.size)
  }

  @Test
  fun `trending movies network error should set error state and stop loading`() = runTest {
    val error = GeneralError.NetworkError
    getMoviesList.setResult(VideoListType.Trending, Result.Failure(error))

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertFalse(state.isLoading)
    assertNotNull(state.error)
  }

  @Test
  fun `trending movies api error should set error state`() = runTest {
    val error = GeneralError.ApiError(message = "Server error", code = 500)
    getMoviesList.setResult(VideoListType.Trending, Result.Failure(error))

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertFalse(state.isLoading)
    assertNotNull(state.error)
  }

  @Test
  fun `trending shows success should add shows section`() = runTest {
    val showCount = 2
    val shows = TestDataFactory.createShows(showCount)
    getTrendingShows.setResult(Result.Success(shows))

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    val showsSection = state.videoSections.find { it.type == SectionType.TrendingShows }
    assertFalse(state.isLoading)
    assertNotNull(showsSection)
    assertEquals(SectionType.TrendingShows.title, showsSection.title)
    assertEquals(showCount, showsSection.items.size)
  }

  @Test
  fun `trending shows error should set error state`() = runTest {
    val error = GeneralError.ApiError(message = "Shows unavailable", code = 503)
    getTrendingShows.setResult(Result.Failure(error))

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNotNull(state.error)
    assertFalse(state.isLoading)
  }

  @Test
  fun `both trending movies and shows success should create both sections`() = runTest {
    val movies = TestDataFactory.createMovies(2)
    val shows = TestDataFactory.createShows(1)
    getMoviesList.setResult(VideoListType.Trending, Result.Success(movies))
    getTrendingShows.setResult(Result.Success(shows))

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    val moviesSection = state.videoSections.find { it.type == SectionType.TrendingMovies }
    val showsSection = state.videoSections.find { it.type == SectionType.TrendingShows }
    assertEquals(2, state.videoSections.size)
    assertNotNull(moviesSection)
    assertNotNull(showsSection)
    assertEquals(2, moviesSection.items.size)
    assertEquals(1, showsSection.items.size)
    assertFalse(state.isLoading)
    assertNull(state.error)
  }

  @Test
  fun `bookmarked movies with items should show section`() = runTest {
    val count = 2
    val bookmarkedMovies = TestDataFactory.createMovies(count)
    getBookmarkedMovies.setBookmarkedMovies(bookmarkedMovies)

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNotNull(state.bookmarkedMovies)
    assertEquals("Bookmarked Movies", state.bookmarkedMovies.title)
    assertEquals(count, state.bookmarkedMovies.items.size)
    assertEquals(SectionType.None, state.bookmarkedMovies.type)
  }

  @Test
  fun `bookmarked movies empty should hide section`() = runTest {
    getBookmarkedMovies.setBookmarkedMovies(emptyList())

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.bookmarkedMovies)
  }

  @Test
  fun `bookmarked movies updates should reflect in state`() = runTest {
    getBookmarkedMovies.setBookmarkedMovies(emptyList())

    val viewModel = createViewModel()
    advanceUntilIdle()

    assertNull(viewModel.state.value.bookmarkedMovies)

    val count = 2
    val newBookmarks = TestDataFactory.createMovies(count)
    getBookmarkedMovies.setBookmarkedMovies(newBookmarks)
    advanceUntilIdle()

    val updatedState = viewModel.state.value
    assertNotNull(updatedState.bookmarkedMovies)
    assertEquals(count, updatedState.bookmarkedMovies.items.size)
  }

  @Test
  fun `bookmarked shows with items should show section`() = runTest {
    val count = 2
    val bookmarkedShows = TestDataFactory.createShows(count)
    getBookmarkedShows.setBookmarkedShows(bookmarkedShows)

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNotNull(state.bookmarkedShows)
    assertEquals("Bookmarked Shows", state.bookmarkedShows.title)
    assertEquals(count, state.bookmarkedShows.items.size)
  }

  @Test
  fun `bookmarked shows empty should hide section`() = runTest {
    getBookmarkedShows.setBookmarkedShows(emptyList())

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.bookmarkedShows)
  }

  @Test
  fun `bookmarked shows updates should reflect in state`() = runTest {
    val count = 1
    val initialShows = TestDataFactory.createShows(count)
    getBookmarkedShows.setBookmarkedShows(initialShows)

    val viewModel = createViewModel()
    advanceUntilIdle()

    assertEquals(count, viewModel.state.value.bookmarkedShows?.items?.size)

    getBookmarkedShows.setBookmarkedShows(emptyList())
    advanceUntilIdle()

    assertNull(viewModel.state.value.bookmarkedShows)
  }

  @Test
  fun `reload should clear error and video sections`() = runTest {
    val error = GeneralError.NetworkError
    getMoviesList.setResult(VideoListType.Trending, Result.Failure(error))

    val viewModel = createViewModel()
    advanceUntilIdle()

    assertNotNull(viewModel.state.value.error)

    val movies = TestDataFactory.createMovies(2)
    getMoviesList.setResult(VideoListType.Trending, Result.Success(movies))

    viewModel.submitAction(HomeAction.Reload)
    advanceUntilIdle()

    val state = viewModel.state.value
    assertNull(state.error)
    assertTrue(state.videoSections.isNotEmpty())
  }

  @Test
  fun `reload should clear existing video sections before loading`() = runTest {
    val initialShows = TestDataFactory.createShows(2)
    getTrendingShows.setResult(Result.Success(initialShows))

    val viewModel = createViewModel()
    advanceUntilIdle()

    assertTrue(viewModel.state.value.videoSections.isNotEmpty())

    val newMovies = TestDataFactory.createMovies(5)
    getMoviesList.setResult(VideoListType.Trending, Result.Success(newMovies))
    getTrendingShows.setResult(Result.Success(emptyList()))

    viewModel.submitAction(HomeAction.Reload)
    advanceUntilIdle()

    val state = viewModel.state.value
    val moviesSection = state.videoSections.find { it.type == SectionType.TrendingMovies }
    val showsSection = state.videoSections.find { it.type == SectionType.TrendingShows }
    assertNotNull(moviesSection)
    assertNotNull(showsSection)
    assertEquals(5, moviesSection.items.size)
    assertEquals(0, showsSection.items.size)
  }

  @Test
  fun `reload does not affect bookmarked sections`() = runTest {
    val bookmarkedMovies = TestDataFactory.createMovies(1)
    getBookmarkedMovies.setBookmarkedMovies(bookmarkedMovies)

    val viewModel = createViewModel()
    advanceUntilIdle()

    assertNotNull(viewModel.state.value.bookmarkedMovies)

    viewModel.submitAction(HomeAction.Reload)
    advanceUntilIdle()

    assertNotNull(viewModel.state.value.bookmarkedMovies)
  }

  @Test
  fun `all data sources successful should populate all sections`() = runTest {
    val movies = TestDataFactory.createMovies(2)
    val shows = TestDataFactory.createShows(1)

    getMoviesList.setResult(VideoListType.Trending, Result.Success(movies))
    getTrendingShows.setResult(Result.Success(shows))
    getBookmarkedMovies.setBookmarkedMovies(movies)
    getBookmarkedShows.setBookmarkedShows(shows)

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value

    assertEquals(2, state.videoSections.size)
    assertNotNull(state.bookmarkedMovies)
    assertNotNull(state.bookmarkedShows)
    assertFalse(state.isLoading)
    assertNull(state.error)
  }

  @Test
  fun `partial failure should show error but load successful sections`() = runTest {
    val error = GeneralError.NetworkError
    val shows = TestDataFactory.createShows(1)

    getMoviesList.setResult(VideoListType.Trending, Result.Failure(error))
    getTrendingShows.setResult(Result.Success(shows))

    val viewModel = createViewModel()
    advanceUntilIdle()

    val state = viewModel.state.value
    val showsSection = state.videoSections.find { it.type == SectionType.TrendingShows }
    assertNotNull(state.error)
    assertNotNull(showsSection)
  }

  private fun createViewModel(): HomeViewModel {
    return HomeViewModel(
      getMoviesList = getMoviesList,
      getTrendingShows = getTrendingShows,
      getBookmarkedMoviesUseCase = getBookmarkedMovies,
      getBookmarkedShowsUseCase = getBookmarkedShows,
    )
  }
}
