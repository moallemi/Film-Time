package io.filmtime.feature.movie.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.filmtime.core.designsystem.theme.FilmTimeTheme
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.core.ui.common.componnents.LoadingCastSectionRow
import io.filmtime.core.ui.common.componnents.VideoSectionRow
import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.Preview
import io.filmtime.data.model.VideoDetail
import io.filmtime.feature.movie.detail.components.VideoThumbnailInfo

@Composable
fun MovieDetailScreen(
  viewModel: MovieDetailViewModel,
  onStreamReady: (String) -> Unit,
  onCastItemClick: (Long) -> Unit,
  onMovieClick: (Int) -> Unit,
  onBackPressed: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val creditState by viewModel.creditState.collectAsStateWithLifecycle()
  val similarState by viewModel.similarState.collectAsStateWithLifecycle()
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
    ErrorContent(
      uiMessage = state.error!!,
      onRetryClick = viewModel::load,
    )
  } else if (videoDetail != null) {
    MovieDetailScreen(
      videoDetail = videoDetail,
      creditState = creditState,
      similarState = similarState,
      onSimilarItemClick = onMovieClick,
    )
  }
}

@Composable
fun MovieDetailScreen(
  videoDetail: VideoDetail,
  creditState: MovieDetailCreditState,
  similarState: MovieDetailSimilarState,
  onSimilarItemClick: (Int) -> Unit,
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      Box {
        var imageHeight by remember { mutableIntStateOf(500) }
        AsyncImage(
          modifier = Modifier
            .onGloballyPositioned {
              imageHeight = it.size.height
            }
            .fillMaxWidth()
            .aspectRatio(2 / 3f)
            .drawWithCache {
              val gradient = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color(0x99000000), Color(0xDD000000)),
                startY = imageHeight.toFloat() / 2,
                endY = imageHeight.toFloat(),
              )
              // Draw the image
              onDrawWithContent {
                drawContent()
                drawRect(gradient, blendMode = BlendMode.Multiply)
              }
            },
          contentScale = ContentScale.Crop,
          model = videoDetail.posterUrl,
          contentDescription = "Poster Image",
        )
        FilmTimeTheme(
          darkTheme = true,
        ) {
          VideoThumbnailInfo(
            modifier = Modifier
              .padding(16.dp)
              .align(Alignment.BottomCenter),
            videoDetail = videoDetail,
          )
        }
      }
    }
    item {
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.titleMedium,
        text = "Cast",
      )
      if (creditState.isLoading) {
        LoadingCastSectionRow(numberOfSections = 10)
      } else if (creditState.credit.isNotEmpty()) {
        LazyRow() {
          items(creditState.credit) { item ->
            CreditRowItem(item = item)
          }
        }
      }
    }
    item {
      VideoSectionRow(
        modifier = Modifier
          .padding(bottom = 16.dp),
        title = "Similar",
        items = similarState.videoItems,
        onMovieClick = {
          onSimilarItemClick(it)
        },
        onShowClick = {},
        onSectionClick = null,
      )
    }
  }
}

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
      style = MaterialTheme.typography.bodySmall,
      modifier = Modifier.padding(vertical = 4.dp),
    )
  }
}

@ThemePreviews
@Composable
private fun MovieDetailScreenPreview() {
  PreviewFilmTimeTheme {
    MovieDetailScreen(
      videoDetail = VideoDetail.Preview,
      creditState = MovieDetailCreditState(
        credit = listOf(
          CreditItem(
            id = 1,
            name = "Actor 1",
            profile = "",
          ),
          CreditItem(
            id = 2,
            name = "Actor 2",
            profile = "",
          ),
          CreditItem(
            id = 3,
            name = "Actor 3",
            profile = "",
          ),
        ),
        isLoading = false,
        error = null,
      ),
      similarState = MovieDetailSimilarState(
        videoItems = listOf(),
      ),
      onSimilarItemClick = {},
    )
  }
}
