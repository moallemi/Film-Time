package io.filmtime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.filmtime.core.ui.navigation.DestinationRoute
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoType
import io.filmtime.feature.home.GRAPH_HOME_ROUTE
import io.filmtime.feature.home.homeGraph
import io.filmtime.feature.movie.detail.movieDetailScreen
import io.filmtime.feature.movie.detail.navigateToMovieDetail
import io.filmtime.feature.movies.moviesGraph
import io.filmtime.feature.player.navigateToPlayer
import io.filmtime.feature.player.playerScreen
import io.filmtime.feature.settings.settingsGraph
import io.filmtime.feature.show.detail.navigateToShowDetail
import io.filmtime.feature.show.detail.showDetailScreen
import io.filmtime.feature.shows.showsGraph
import io.filmtime.feature.trakt.login.navigateToTraktLogin
import io.filmtime.feature.trakt.login.traktLoginScreen
import io.filmtime.feature.video.thumbnail.grid.navigateToVideoThumbnailGridScreen
import io.filmtime.feature.video.thumbnail.grid.videoThumbnailGridScreen

@Composable
fun FilmTimeNavHost(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  NavHost(
    modifier = modifier,
    navController = navController,
    startDestination = GRAPH_HOME_ROUTE.route,
  ) {
    homeGraph(
      onMovieClick = navController::navigateToMovieDetail,
      onShowClick = navController::navigateToShowDetail,
      onTrendingMoviesClick = { rootRoute ->
        navController.navigateToVideoThumbnailGridScreen(
          rootRoute = rootRoute,
          videoType = VideoType.Movie,
          listType = VideoListType.Trending,
        )
      },
      onTrendingShowsClick = { rootRoute ->
        navController.navigateToVideoThumbnailGridScreen(
          rootRoute = rootRoute,
          videoType = VideoType.Show,
          listType = VideoListType.Trending,
        )
      },
      nestedGraphs = { rootRoute ->
        videoThumbnailGridScreen(rootRoute, navController)
        movieDetailScreen(rootRoute, navController)
        showDetailScreen(rootRoute, navController)
        playerScreen(rootRoute)
      },
    )

    moviesGraph(
      onMovieClick = navController::navigateToMovieDetail,
      onSectionClick = { rootRoute, listType ->
        navController.navigateToVideoThumbnailGridScreen(
          rootRoute = rootRoute,
          videoType = VideoType.Movie,
          listType = listType,
        )
      },
      nestedGraphs = { rootRoute ->
        videoThumbnailGridScreen(rootRoute, navController)
        movieDetailScreen(rootRoute, navController)
      },
    )

    showsGraph(
      onShowClick = navController::navigateToShowDetail,
      onSectionClick = { rootRoute, listType ->
        navController.navigateToVideoThumbnailGridScreen(
          rootRoute = rootRoute,
          videoType = VideoType.Show,
          listType = listType,
        )
      },
      nestedGraphs = { rootRoute ->
        videoThumbnailGridScreen(rootRoute, navController)
        showDetailScreen(rootRoute = rootRoute, navController)
        playerScreen(rootRoute = rootRoute)
      },
    )

    settingsGraph(
      onTraktLoginClick = navController::navigateToTraktLogin,
      nestedGraphs = { rootRoute ->
        traktLoginScreen(rootRoute, navController)
      },
    )
  }
}

private fun NavGraphBuilder.videoThumbnailGridScreen(
  rootRoute: DestinationRoute,
  navController: NavHostController,
) {
  videoThumbnailGridScreen(
    rootRoute = rootRoute,
    onMovieClick = navController::navigateToMovieDetail,
    onShowClick = navController::navigateToShowDetail,
    onBack = navController::popBackStack,
  )
}

private fun NavGraphBuilder.movieDetailScreen(
  rootRoute: DestinationRoute,
  navController: NavHostController,
) {
  movieDetailScreen(
    rootRoute = rootRoute,
    onStreamReady = navController::navigateToPlayer,
    onCastItemClick = { _, _ -> },
    onMovieClick = navController::navigateToMovieDetail,
    onBack = navController::popBackStack,
  )
}

private fun NavGraphBuilder.showDetailScreen(
  rootRoute: DestinationRoute,
  navController: NavHostController,
) {
  showDetailScreen(
    rootRoute = rootRoute,
    onStreamReady = navController::navigateToPlayer,
    onCastItemClick = { _, _ -> },
    onSimilarClick = navController::navigateToShowDetail,
    onBack = navController::popBackStack,
  )
}

private fun NavGraphBuilder.traktLoginScreen(
  rootRoute: DestinationRoute,
  navController: NavHostController,
) {
  traktLoginScreen(
    rootRoute = rootRoute,
    onBack = navController::popBackStack,
  )
}
