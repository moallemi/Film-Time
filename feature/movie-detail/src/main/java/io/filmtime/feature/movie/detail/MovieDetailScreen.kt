package io.filmtime.feature.movie.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.VideoDetail

@Composable
fun MovieDetailScreen(
  viewModel: MovieDetailViewModel,
  onStreamReady: (String) -> Unit,
  onBackPressed: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val videoDetail = state.videoDetail
  val navigateToPlayer by viewModel.navigateToPlayer.collectAsStateWithLifecycle(null)

  LaunchedEffect(key1 = navigateToPlayer) {
    navigateToPlayer?.let { streamUrl ->
      onStreamReady(streamUrl)
    }
  }

  if (state.isLoading) {
    CircularProgressIndicator(
      modifier = Modifier.wrapContentSize(),
    )
  } else if (state.error != null) {
    ShowError(state.error!!, state.message!!) {
      viewModel.load()
    }
  } else if (videoDetail != null) {
    MovieDetailContent(
      videoDetail = videoDetail,
      state = state,
      onBackPressed = onBackPressed,
      onPlayPressed = viewModel::loadStreamInfo,
    )
  }
}

@Composable
fun ShowError(error: GeneralError, message: String, onRefresh: () -> Unit) {
  val composition by rememberLottieComposition(
    LottieCompositionSpec.RawRes(
      if (error is GeneralError.NetworkError) {
        R.raw.network_lost
      } else {
        R.raw.not_found
      },
    ),
  )
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 13.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,

  ) {
    LottieAnimation(
      modifier = Modifier.size(size = 240.dp),
      composition = composition,
    )
    Spacer(modifier = Modifier.size(10.dp))
    Text(
      text = message,
      textAlign = TextAlign.Center,
      style = typography.bodyLarge,
    )

    Spacer(modifier = Modifier.size(60.dp))
    Button(
      onClick = onRefresh,
    ) {
      Text(text = "Refresh")
    }
  }
}

@Composable
fun MovieDetailContent(
  videoDetail: VideoDetail,
  state: MovieDetailState,
  onBackPressed: () -> Unit,
  onPlayPressed: () -> Unit,
) {
  Column(
    modifier = Modifier.verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Box(
      modifier = Modifier
        .height(300.dp),
    ) {
      AsyncImage(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16f / 9f),
        contentScale = ContentScale.FillWidth,
        model = videoDetail.coverUrl,
        contentDescription = null,
        alignment = Alignment.BottomCenter,
      )
      Card(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(start = 16.dp),
      ) {
        AsyncImage(
          modifier = Modifier
            .size(120.dp, 200.dp),
          contentScale = ContentScale.Crop,
          model = videoDetail.posterUrl,
          contentDescription = null,
        )
      }
      IconButton(onClick = onBackPressed) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "back")
      }
    }

    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      style = typography.titleLarge,
      text = videoDetail.title,
    )
    Text(
      modifier = Modifier.padding(horizontal = 16.dp),

      text = videoDetail.description,
    )

    Button(
      onClick = onPlayPressed,
    ) {
      if (state.isStreamLoading) {
        CircularProgressIndicator(
          modifier = Modifier.size(16.dp),
          color = Color.White,
          strokeWidth = 2.dp,
        )
      } else {
        Text(text = "Play")
      }
    }

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
