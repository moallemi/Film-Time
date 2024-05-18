package io.filmtime.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector
import io.filmtime.R

enum class RootDestination(
  val rootScreen: RootScreen,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val title: Int,
) {
  Home(
    rootScreen = RootScreen.Home,
    selectedIcon = Icons.Rounded.Home,
    unselectedIcon = Icons.Outlined.Home,
    title = R.string.home,
  ),
  Movies(
    rootScreen = RootScreen.Movies,
    selectedIcon = Icons.Rounded.Movie,
    unselectedIcon = Icons.Outlined.Movie,
    title = R.string.movies,
  ),
  Shows(
    rootScreen = RootScreen.Shows,
    selectedIcon = Icons.Rounded.Subscriptions,
    unselectedIcon = Icons.Outlined.Subscriptions,
    title = R.string.series,
  ),
  Search(
    rootScreen = RootScreen.Search,
    selectedIcon = Icons.Rounded.Search,
    unselectedIcon = Icons.Outlined.Search,
    title = R.string.search,
  ),
  Settings(
    rootScreen = RootScreen.Settings,
    selectedIcon = Icons.Rounded.Settings,
    unselectedIcon = Icons.Outlined.Settings,
    title = R.string.settings,
  ),
}
