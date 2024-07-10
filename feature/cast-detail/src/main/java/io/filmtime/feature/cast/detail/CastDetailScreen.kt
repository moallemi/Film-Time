package io.filmtime.feature.cast.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CastDetailScreen() {
  val viewModel: CastDetailViewModel = viewModel()

  val state by viewModel.state.collectAsStateWithLifecycle()

  Scaffold { padding ->
    Box(
      modifier = Modifier.padding(padding),
    ) {
    }
  }
}
