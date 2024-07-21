package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.filmtime.data.model.Person
import io.filmtime.data.model.SearchResult

@Composable
fun PersonSearchCard(item: SearchResult.Person) {
  Column(
    modifier = Modifier
      .width(100.dp)
      .wrapContentHeight(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    AsyncImage(
      modifier = Modifier
        .size(100.dp)
        .clip(CircleShape)
        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), CircleShape),
      contentScale = ContentScale.Crop,
      model = item.imageUrl,
      contentDescription = "credit_profile",
      alignment = Alignment.Center,
    )
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, bottom = 4.dp),
      text = item.name,
      style = MaterialTheme.typography.bodySmall,
      textAlign = TextAlign.Center,
    )
  }
}

@Preview
@Composable
private fun PersonSearchCardPreview() {
  PersonSearchCard(item = SearchResult.Person(name = "Name", imageUrl = ""))
}
