package io.filmtime.feature.show.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import io.filmtime.core.designsystem.theme.FilmTimeTheme
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.core.ui.common.componnents.VideoDescription
import io.filmtime.core.ui.common.componnents.VideoInfo
import io.filmtime.core.ui.common.componnents.VideoThumbnailInfo
import io.filmtime.core.ui.common.componnents.VideoThumbnailPoster
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.Preview
import io.filmtime.data.model.PreviewShow
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoGenre
import io.filmtime.data.model.VideoType
import io.filmtime.feature.credits.components.CreditsRow
import io.filmtime.feature.show.detail.components.SeasonsSection
import io.filmtime.feature.similar.SimilarVideosRow

@Composable
internal fun ShowDetailScreen(
  viewModel: ShowDetailViewModel,
  onCastItemClick: (Long) -> Unit,
  onShowClick: (Int) -> Unit,
  onGenreClick: (VideoGenre) -> Unit,
  onBackPressed: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  ShowDetailScreen(
    state = state,
    onRetry = viewModel::load,
    onShowClick = onShowClick,
    onAddBookmark = viewModel::addBookmark,
    onRemoveBookmark = viewModel::removeBookmark,
    onSeasonChange = viewModel::changeSeason,
    addToHistory = viewModel::addEpisodeToHistory,
    removeFromHistory = viewModel::removeEpisodeFromHistory,
    onGenreClick = onGenreClick,
    onPrimaryButtonClick = {},
    onEpisodeClick = {},
  )
}

@Composable
private fun ShowDetailScreen(
  state: ShowDetailState,
  onRetry: () -> Unit,
  onShowClick: (Int) -> Unit,
  onAddBookmark: () -> Unit,
  onRemoveBookmark: () -> Unit,
  onGenreClick: (VideoGenre) -> Unit,
  onSeasonChange: (Int) -> Unit,
  onPrimaryButtonClick: () -> Unit,
  onEpisodeClick: (EpisodeThumbnail) -> Unit,
  addToHistory: (EpisodeThumbnail) -> Unit,
  removeFromHistory: (EpisodeThumbnail) -> Unit,
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
      ratings = state.ratings,
      isBookmarked = state.isBookmarked,
      onAddBookmark = onAddBookmark,
      onGenreClick = onGenreClick,
      onRemoveBookmark = onRemoveBookmark,
      seasonsState = state.seasonsState,
      onSeasonChange = onSeasonChange,
      addToHistory = addToHistory,
      onEpisodeClick = onEpisodeClick,
      removeFromHistory = removeFromHistory,
      primaryButton = {
        FilmTimeFilledButton(
          modifier = Modifier
            .fillMaxWidth(),
          onClick = onPrimaryButtonClick,
        ) {
          Text("Play First Episode")
        }
      },
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
  seasonsState: SeasonsState,
  ratings: Ratings?,
  isBookmarked: Boolean,
  onAddBookmark: () -> Unit,
  onGenreClick: (VideoGenre) -> Unit,
  onRemoveBookmark: () -> Unit,
  onSeasonChange: (Int) -> Unit,
  onEpisodeClick: (EpisodeThumbnail) -> Unit,
  addToHistory: (EpisodeThumbnail) -> Unit,
  removeFromHistory: (EpisodeThumbnail) -> Unit,
  primaryButton: @Composable () -> Unit,
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
            ratings = ratings,
            primaryButton = primaryButton,
            traktHistoryButton = {
            },
          )
        }
      }
    }
    item(
      key = "seasons",
    ) {
      SeasonsSection(
        isLoading = seasonsState.isLoading,
        seasons = seasonsState.seasons,
        seasonsNumber = videoDetail.seasonsNumber ?: 0,
        error = seasonsState.error,
        onSeasonChange = onSeasonChange,
        onRetryClick = onSeasonChange,
        onEpisodeClick = onEpisodeClick,
        addToHistory = addToHistory,
        removeFromHistory = removeFromHistory,
      )
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
        onGenreClick = onGenreClick,
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
      ratings = Ratings.Preview,
      isBookmarked = false,
      onAddBookmark = {},
      onRemoveBookmark = {},
      onGenreClick = {},
      seasonsState = SeasonsState(
        seasons = mapOf(
          1 to listOf(EpisodeThumbnail.Preview, EpisodeThumbnail.Preview),
          2 to listOf(EpisodeThumbnail.Preview, EpisodeThumbnail.Preview),
          3 to listOf(EpisodeThumbnail.Preview, EpisodeThumbnail.Preview),
        ),
      ),
      onSeasonChange = {},
      onEpisodeClick = {},
      addToHistory = {},
      removeFromHistory = {},
      primaryButton = {
        FilmTimeFilledButton(
          modifier = Modifier
            .fillMaxWidth(),
          onClick = { },
        ) {
          Text("Play First Episode")
        }
      },
      credits = {
        Text("Credit goes here")
      },
      similar = {
        Text("Similar goes here")
      },
    )
  }
}
