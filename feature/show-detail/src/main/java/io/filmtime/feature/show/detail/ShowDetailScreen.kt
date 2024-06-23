package io.filmtime.feature.show.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.designsystem.theme.FilmTimeTheme
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.core.ui.common.componnents.VideoDescription
import io.filmtime.core.ui.common.componnents.VideoInfo
import io.filmtime.core.ui.common.componnents.VideoThumbnailInfo
import io.filmtime.core.ui.common.componnents.VideoThumbnailPoster
import io.filmtime.data.model.PreviewShow
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoType
import io.filmtime.feature.credits.components.CreditsRow
import io.filmtime.feature.similar.SimilarVideosRow

@Composable
fun ShowDetailScreen(
  viewModel: ShowDetailViewModel,
  onCastItemClick: (Long) -> Unit,
  onShowClick: (Int) -> Unit,
  onBackPressed: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  ShowDetailScreen(
    state = state,
    onRetry = viewModel::load,
    onShowClick = onShowClick,
    onAddBookmark = viewModel::addBookmark,
    onRemoveBookmark = viewModel::removeBookmark,
  )
}

@Composable
fun ShowDetailScreen(
  state: ShowDetailState,
  onRetry: () -> Unit,
  onShowClick: (Int) -> Unit,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
) {
  val videoDetail = state.videoDetail

  if (state.isLoading) {
    CircularProgressIndicator(
      modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(),
    )
  } else if (state.error != null) {
    ErrorContent(
      uiMessage = state.error,
      onRetryClick = onRetry,
    )
  } else if (videoDetail != null) {
    ShowDetailContent(
      videoDetail = videoDetail,
      isBookmarked = state.isBookmarked,
      onAddBookmark = onAddBookmark,
      onRemoveBookmark = onRemoveBookmark,
      credits = {
        CreditsRow(
          tmdbId = videoDetail.ids.tmdbId ?: 0,
          videoType = VideoType.Show,
        )
      },
      similar = {
        SimilarVideosRow(
          tmdbId = videoDetail.ids.tmdbId ?: 0,
          videoType = VideoType.Show,
          onVideoClick = onShowClick,
        )
      },
    )
  }
}

@Composable
private fun ShowDetailContent(
  videoDetail: VideoDetail,
  isBookmarked: Boolean,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
  credits: @Composable () -> Unit,
  similar: @Composable () -> Unit,
) {
  var imageHeight by remember { mutableIntStateOf(4000) }
  val density = LocalDensity.current
  val boxHeight by remember(imageHeight) {
    derivedStateOf {
      with(density) {
        (imageHeight + 150).toDp()
      }
    }
  }
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item(
      key = "header",
    ) {
      FilmTimeTheme(
        darkTheme = true,
      ) {
        Box(
          modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .height(boxHeight),
        ) {
          VideoThumbnailPoster(
            modifier = Modifier
              .onGloballyPositioned {
                imageHeight = it.size.height
              },
            posterUrl = videoDetail.posterUrl,
            height = imageHeight,
          )
          VideoThumbnailInfo(
            modifier = Modifier
              .padding(16.dp)
              .align(Alignment.BottomCenter),
            videoDetail = videoDetail,
            isBookmarked = isBookmarked,
            onAddBookmark = onAddBookmark,
            onRemoveBookmark = onRemoveBookmark,
            traktHistoryButton = {
            },
          )
        }
      }
    }
    item(
      key = "credits",
    ) {
      credits()
    }
    item(
      key = "similar",
    ) {
      similar()
    }
    item(
      key = "description",
    ) {
      VideoDescription(
        videoDetail = videoDetail,
      )
    }
    item(
      key = "info",
    ) {
      VideoInfo(
        videoDetail = videoDetail,
      )
    }
  }
}

@ThemePreviews
@Composable
private fun ShowDetailScreenPreview() {
  PreviewFilmTimeTheme {
    ShowDetailContent(
      videoDetail = VideoDetail.PreviewShow,
      isBookmarked = false,
      onAddBookmark = {},
      onRemoveBookmark = {},
      credits = {
        Text("Credit goes here")
      },
      similar = {
        Text("Similar goes here")
      },
    )
  }
}
