package io.filmtime.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.data.model.VideoThumbnail

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  if (state.isLoading) {
    CircularProgressIndicator(
      modifier = Modifier
        .wrapContentSize(),
    )
  } else {
    LazyColumn(
      contentPadding = PaddingValues(top = 16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(state.videoSections) { videoSection ->
        VideoSectionRow(
          title = videoSection.title,
          items = videoSection.items,
        )
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoSectionRow(
  title: String,
  items: List<VideoThumbnail>,
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
        )
      }
    }
  }
}
