package io.filmtime.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen() {
  Box(
    modifier = Modifier
      .fillMaxSize(),
  ) {
    Text(
      modifier = Modifier
        .align(Alignment.Center),
      text = "Settings",
      style = MaterialTheme.typography.titleLarge,
    )
  }
}
