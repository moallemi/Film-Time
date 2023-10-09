package io.filmtime.feature.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType
import io.filmtime.domain.trakt.auth.TraktAuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onTraktClick: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val traktState by viewModel.traktState.collectAsStateWithLifecycle()

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
          contentPadding = PaddingValues(top = 16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          items(state.videoSections) { videoSection ->
            VideoSectionRow(
              title = videoSection.title,
              items = videoSection.items,
              onMovieClick = onMovieClick,
              onShowClick = onShowClick,
            )
          }
        }
      }
    }
  }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoSectionRow(
  title: String,
  items: List<VideoThumbnail>,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
) {
  Column {
    Text(
      modifier = Modifier
        .padding(start = 16.dp),
      text = title,
      style = MaterialTheme.typography.titleMedium,
    )
    LazyRow(
      modifier = Modifier
        .height(200.dp)
        .fillMaxWidth(),
      contentPadding = PaddingValues(16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(items) { item ->
        VideoThumbnailCard(
          modifier = Modifier
            .testTag("discover_carousel_item")
            .animateItemPlacement()
            .fillParentMaxHeight()
            .aspectRatio(2 / 3f),
          videoThumbnail = item,
          onClick = {
            item.ids.tmdbId?.let {
              when (item.type) {
                VideoType.Movie -> onMovieClick(it)
                VideoType.Show -> onShowClick(it)
              }
            } ?: run {
              Log.e("HomeScreen", "tmdbId is null")
            }
          },
        )
      }
    }
  }
}

@Composable
fun LoadingVideoSectionRow(
  numberOfSections: Int,
) {
  LazyColumn(
    contentPadding = PaddingValues(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(numberOfSections) {
      Box(
        modifier = Modifier
          .padding(start = 16.dp)
          .size(50.dp, 20.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(shimmerBrush()),
      )
      LazyRow(
        modifier = Modifier
          .height(200.dp)
          .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(10) {
          Box(
            modifier = Modifier
              .fillParentMaxHeight()
              .aspectRatio(2 / 3f)
              .clip(RoundedCornerShape(16.dp))
              .background(shimmerBrush()),
          )
        }
      }
    }
  }
}
