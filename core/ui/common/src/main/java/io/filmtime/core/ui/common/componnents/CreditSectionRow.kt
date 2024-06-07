package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.filmtime.data.model.CreditItem

@Composable
fun CreditRowItem(item: CreditItem) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(horizontal = 6.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    AsyncImage(
      modifier = Modifier
        .size(60.dp)
        .clip(CircleShape) // clip to the circle shape
        .border(1.dp, Color.Transparent, CircleShape),
      contentScale = ContentScale.Crop,
      model = item.profile,
      contentDescription = "credit_profile",
      alignment = Alignment.Center,
    )
    Text(
      text = item.name,
      style = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
        color = Color.Black,
      ),
      modifier = Modifier.padding(vertical = 4.dp),
    )
  }
}
