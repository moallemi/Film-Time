package io.filmtime.feature.movie.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
fun MovieDetailScreen(
  viewModel: MovieDetailViewModel,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val videoDetail = state.videoDetail

  if (state.isLoading) {
    CircularProgressIndicator()
  } else if (videoDetail != null) {
    Column(
      modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {


      AsyncImage(
        modifier = Modifier
          .width(200.dp)
          .height(300.dp),
        model = videoDetail.posterUrl,
        contentDescription = null,
      )

      AsyncImage(
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp),
        model = videoDetail.coverUrl,
        contentDescription = null,
      )

      Text(text = videoDetail.title)
      Text(text = videoDetail.description)

      Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        Text(text = "Year: ${videoDetail.year}")
        Text(text = "Original language: ${videoDetail.originalLanguage}")
        Text(text = videoDetail.spokenLanguages.joinToString(", "))
      }
      Text(text = "Genres")
      Text(text = videoDetail.genres.joinToString(", "))
    }
  }
}
