package io.filmtime.feature.movie.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import io.filmtime.core.ui.common.componnents.CreditsRow
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.core.ui.common.componnents.VideoSectionRow
import io.filmtime.data.model.Person
import io.filmtime.data.model.Preview
import io.filmtime.data.model.PreviewCast
import io.filmtime.data.model.PreviewCrew
import io.filmtime.data.model.VideoDetail
import io.filmtime.feature.movie.detail.components.VideoDescription
import io.filmtime.feature.movie.detail.components.VideoInfo
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
      modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(),
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
      onCreditsRetry = viewModel::loadCredits,
    )
  }
}

@Composable
private fun MovieDetailScreen(
  videoDetail: VideoDetail,
  creditState: MovieDetailCreditState,
  similarState: MovieDetailSimilarState,
  onSimilarItemClick: (Int) -> Unit,
  onCreditsRetry: () -> Unit,
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
      CreditsRow(
        isLoading = creditState.isLoading,
        credits = creditState.credit,
        error = creditState.error,
        onRetryClick = onCreditsRetry,
      )
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
    item {
      VideoDescription(
        videoDetail = videoDetail,
      )
    }
    item {
      VideoInfo(
        videoDetail = videoDetail,
      )
    }
  }
}

@ThemePreviews
@Composable
private fun MovieDetailScreenPreview() {
  PreviewFilmTimeTheme {
    MovieDetailScreen(
      videoDetail = VideoDetail.Preview,
      creditState = MovieDetailCreditState(
        credit = listOf(Person.PreviewCast, Person.PreviewCast, Person.PreviewCrew),
        isLoading = false,
        error = null,
      ),
      similarState = MovieDetailSimilarState(
        videoItems = listOf(),
      ),
      onSimilarItemClick = {},
      onCreditsRetry = {},
    )
  }
}
