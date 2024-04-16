package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.filmtime.core.ui.common.graphics.shimmerBrush

@Composable
fun LoadingCastSectionRow(
  numberOfSections: Int,
) {
  LazyRow() {
    items(numberOfSections) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Box(
          modifier = Modifier
            .size(60.dp)
            .clip(CircleShape) // clip to the circle shape
            .border(1.dp, Color.Transparent, CircleShape)
            .background(shimmerBrush()),
        )
        Box(
          modifier = Modifier
            .size(50.dp, 10.dp)
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(shimmerBrush()),
        )
      }
    }
  }
}
