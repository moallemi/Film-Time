package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.filmtime.core.ui.common.graphics.shimmerBrush

@Composable
fun LoadingVideoSectionRow(
  numberOfSections: Int,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    modifier = modifier,
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
