package io.filmtime.feature.movie.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import io.filmtime.core.designsystem.composable.FilmTimeFilledButton
import io.filmtime.core.designsystem.composable.FilmTimeFilledTonalButton
import io.filmtime.core.designsystem.theme.FilmTimeTheme
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.core.ui.common.componnents.VideoDescription
import io.filmtime.core.ui.common.componnents.VideoInfo
import io.filmtime.core.ui.common.componnents.VideoThumbnailInfo
import io.filmtime.core.ui.common.componnents.VideoThumbnailPoster
import io.filmtime.data.model.Preview
import io.filmtime.data.model.PreviewMovie
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoType
import io.filmtime.feature.credits.components.CreditsRow
import io.filmtime.feature.similar.SimilarVideosRow
import io.filmtime.feature.trakt.buttons.addremovehistory.TraktAddRemoveHistoryButton

@Composable
fun MovieDetailScreen(
  viewModel: MovieDetailViewModel,
  onStreamReady: (String) -> Unit,
  onCastItemClick: (Long) -> Unit,
  onMovieClick: (Int) -> Unit,
  onBackPressed: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val navigateToPlayer by viewModel.navigateToPlayer.collectAsStateWithLifecycle(null)

  LaunchedEffect(key1 = navigateToPlayer) {
    navigateToPlayer?.let { streamUrl ->
      onStreamReady(streamUrl)
    }
  }

  MovieDetailScreen(
    state = state,
    onRetry = viewModel::loadMovieDetail,
    onMovieClick = onMovieClick,
    onAddBookmark = viewModel::addBookmark,
    onRemoveBookmark = viewModel::removeBookmark,
    onPLayClick = {
    },
  )
}

@Composable
fun MovieDetailScreen(
  state: MovieDetailState,
  onRetry: () -> Unit,
  onMovieClick: (Int) -> Unit,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
  onPLayClick: () -> Unit,
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
    MovieDetailContent(
      videoDetail = videoDetail,
      ratings = state.ratings,
      isBookmarked = state.isBookmarked,
      onAddBookmark = onAddBookmark,
      onRemoveBookmark = onRemoveBookmark,
      primaryButton = {
        FilmTimeFilledButton(
          modifier = Modifier
            .fillMaxWidth(),
          onClick = onPLayClick,
        ) {
          Text("Play")
        }
      },
      credits = {
        CreditsRow(
          tmdbId = videoDetail.ids.tmdbId ?: 0,
          videoType = VideoType.Movie,
        )
      },
      similar = {
        SimilarVideosRow(
          tmdbId = videoDetail.ids.tmdbId ?: 0,
          videoType = VideoType.Movie,
          onVideoClick = onMovieClick,
        )
      },
      traktHistoryButton = {
        TraktAddRemoveHistoryButton(
          modifier = Modifier
            .weight(1f),
          videoType = VideoType.Movie,
          tmdbId = videoDetail.ids.tmdbId ?: 0,
        )
      },
    )
  }
}

@Composable
private fun MovieDetailContent(
  videoDetail: VideoDetail,
  ratings: Ratings?,
  isBookmarked: Boolean,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
  primaryButton: @Composable () -> Unit,
  credits: @Composable () -> Unit,
  similar: @Composable () -> Unit,
  traktHistoryButton: @Composable RowScope.() -> Unit,
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
            primaryButton = primaryButton,
            traktHistoryButton = traktHistoryButton,
            ratings = ratings,
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
private fun MovieDetailScreenPreview() {
  PreviewFilmTimeTheme {
    MovieDetailContent(
      videoDetail = VideoDetail.PreviewMovie,
      isBookmarked = false,
      onAddBookmark = {},
      onRemoveBookmark = {},
      primaryButton = {
        FilmTimeFilledButton(
          modifier = Modifier
            .fillMaxWidth(),
          onClick = { },
        ) {
          Text("Play")
        }
      },
      credits = {
        Text("Credit goes here")
      },
      similar = {
        Text("Similar goes here")
      },
      ratings = Ratings.Preview,
      traktHistoryButton = {
        FilmTimeFilledTonalButton(
          modifier = Modifier
            .weight(1f),
          onClick = {},
        ) {
          Text("Add to history")
        }
      },
    )
  }
}
