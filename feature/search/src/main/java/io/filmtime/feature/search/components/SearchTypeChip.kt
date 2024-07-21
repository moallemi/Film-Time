package io.filmtime.feature.search.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.filmtime.data.model.SearchType

@Composable
fun SearchTypeChip(
  selectedSearchType: SearchType,
  onChange: (SearchType) -> Unit,
) {
  Row(
    modifier = Modifier
      .padding(horizontal = 16.dp),
  ) {
    SearchType.entries.forEach {
      FilterChip(
        selected = selectedSearchType == it,
        onClick = { onChange(it) },
        label = { Text(it.name) },
      )
      Spacer(modifier = Modifier.width(4.dp))
    }
  }
}
