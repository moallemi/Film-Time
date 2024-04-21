package io.filmtime.core.designsystem.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmTimeSmallTopAppBar(
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: ImageVector? = null,
  onNavClick: (() -> Unit)? = null,
  actions: @Composable RowScope.() -> Unit = {},
  windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
  colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
  scrollBehavior: TopAppBarScrollBehavior? = null,
) {
  TopAppBar(
    title = title,
    modifier = modifier,
    navigationIcon = {
      if (onNavClick != null) {
        IconButton(
          onClick = onNavClick,
        ) {
          Icon(
            imageVector = navigationIcon ?: Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = "TopAppBar navigation icon",
          )
        }
      }
    },
    actions = actions,
    windowInsets = windowInsets,
    colors = colors,
    scrollBehavior = scrollBehavior,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmTimeSmallTopAppBar(
  title: String,
  modifier: Modifier = Modifier,
  navigationIcon: ImageVector? = null,
  onNavClick: (() -> Unit)? = null,
  actions: @Composable RowScope.() -> Unit = {},
  windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
  colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
  scrollBehavior: TopAppBarScrollBehavior? = null,
) {
  FilmTimeSmallTopAppBar(
    title = {
      Text(
        text = title,
      )
    },
    modifier = modifier,
    navigationIcon = navigationIcon,
    onNavClick = onNavClick,
    actions = actions,
    windowInsets = windowInsets,
    colors = colors,
    scrollBehavior = scrollBehavior,
  )
}
