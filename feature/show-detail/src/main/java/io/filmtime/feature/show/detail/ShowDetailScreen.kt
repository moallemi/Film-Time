package io.filmtime.feature.show.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun ShowDetailScreen(
  viewModel: ShowDetailViewModel,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val videoDetail = state.videoDetail

  if (state.isLoading) {
    CircularProgressIndicator()
  } else if (state.message != null) {
    Text(text = state.message!!)
  } else if (videoDetail != null) {
    Column(
      modifier = Modifier
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      AsyncImage(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f),
        contentScale = ContentScale.Crop,
        model = videoDetail.coverUrl,
        contentDescription = null,
      )

      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.titleLarge,
        text = videoDetail.title,
      )
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),

        text = videoDetail.description,
      )

      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(text = "Year: ${videoDetail.year}")
        Text(text = "Original language: ${videoDetail.originalLanguage}")
        Text(text = videoDetail.spokenLanguages.joinToString(", "))
      }
      Text(
        modifier = Modifier.padding(start = 16.dp),
        text = "Genres",
      )
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = videoDetail.genres.joinToString(", "),
      )
    }
  }
}
