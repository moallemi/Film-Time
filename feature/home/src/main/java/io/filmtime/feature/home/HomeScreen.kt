package io.filmtime.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.ui.common.componnents.LoadingVideoSectionRow
import io.filmtime.core.ui.common.componnents.VideoSectionRow
import io.filmtime.domain.trakt.auth.TraktAuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onTraktClick: () -> Unit,
  onTrendingMoviesClick: () -> Unit,
  onTrendingShowsClick: () -> Unit,
) {
  val viewModel = hiltViewModel<HomeViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()
  val traktState by viewModel.traktAuthState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("FilmTime") },
        actions = {
          IconButton(onClick = onTraktClick) {
            Icon(
              painter = painterResource(id = R.drawable.trakt),
              contentDescription = "trakt",
              tint = if (traktState == TraktAuthState.LoggedIn) Color.Green else MaterialTheme.colorScheme.onBackground,
            )
          }
        },
      )
    },
  ) {
    Box(modifier = Modifier.padding(it)) {
      if (state.isLoading) {
        LoadingVideoSectionRow(numberOfSections = 2)
      } else {
        LazyColumn(
          contentPadding = PaddingValues(vertical = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          items(state.videoSections) { videoSection ->
            VideoSectionRow(
              title = videoSection.title,
              items = videoSection.items,
              onMovieClick = onMovieClick,
              onShowClick = onShowClick,
              onSectionClick = {
                when (videoSection.type) {
                  SectionType.TrendingMovies -> onTrendingMoviesClick()
                  SectionType.TrendingShows -> onTrendingShowsClick()
                }
              },
            )
          }
        }
      }
    }
  }
}
