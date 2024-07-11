package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun VideoSearchCard(
  modifier: Modifier = Modifier,
  imageUrl: String,
  title: String,
  onClick: () -> Unit,
) {
  Card(
    onClick = onClick,
    modifier = modifier,
  ) {
    VideoSearchCardContent(imageUrl = imageUrl, title = title)
  }
}

@Composable
private fun VideoSearchCardContent(
  imageUrl: String,
  title: String,
) {
  Box(
    modifier = Modifier.fillMaxSize(),
  ) {
    Text(
      text = title,
      modifier = Modifier
        .align(Alignment.Center)
        .padding(8.dp),
      style = MaterialTheme.typography.bodySmall,
      textAlign = TextAlign.Center,
    )
    AsyncImage(
      model = imageUrl,
      contentDescription = title,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )
  }
}
