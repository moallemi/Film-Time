package io.filmtime.tv.ui.detail.movie

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import io.filmtime.data.model.VideoDetail
import io.filmtime.tv.R
import io.filmtime.tv.ui.component.CastRowLoading
import io.filmtime.tv.ui.component.CastsRow
import io.filmtime.tv.ui.component.DetailHeader
import io.filmtime.tv.ui.component.DetailHeaderLoadingPlaceholder
import io.filmtime.tv.ui.component.DetailPoster
import io.filmtime.tv.ui.component.ErrorScreen
import io.filmtime.tv.ui.component.MovieInformation
import io.filmtime.tv.ui.component.PosterLoadingPlaceholder
import io.filmtime.tv.ui.component.SimilarSection

@Composable
fun MovieDetailScreen(modifier: Modifier = Modifier) {
  val viewModel: MovieDetailViewModel = hiltViewModel()
  val uiState by viewModel.state.collectAsStateWithLifecycle()
  MovieDetailContent(
    modifier = modifier,
    uiState = uiState,
    onReload = viewModel::loadMovieDetail,
    movieBackgroundPoster = { coverUrl ->
      DetailPoster(
        coverUrl = coverUrl,
        scrimColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize(),
      )
    },
    headerContent = { detail ->
      DetailHeader(
        videoDetail = detail,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(start = 50.dp),
        isBookmark = uiState.isBookmarked,
        onRemoveBookmark = { viewModel.removeBookmark() },
        onAddBookmark = { viewModel.addBookmark() },
      )
    },
    castsContent = {
      CastsRow(tmdbId = it)
    },
    similarContent = {
      SimilarSection(tmdbId = it)
    },
    informationContent = { detail ->
      MovieInformation(
        videoDetail = detail,
        ratings = uiState.ratings,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .focusable(),
      )
    },
  )
}

@Composable
private fun MovieDetailContent(
  modifier: Modifier = Modifier,
  uiState: MovieDetailState,
  movieBackgroundPoster: @Composable (coverUrl: String) -> Unit,
  headerContent: @Composable (detail: VideoDetail) -> Unit,
  castsContent: @Composable (tmdbId: Int) -> Unit,
  similarContent: @Composable (tmdbId: Int) -> Unit,
  informationContent: @Composable (detail: VideoDetail) -> Unit,
  onReload: () -> Unit = {},
) {
  Box(modifier = modifier) {
    when {
      uiState.isLoading -> DetailContentLoading()
      uiState.error != null -> ErrorScreen(
        message = uiState.error,
        actionTitle = stringResource(R.string.btn_retry),
        onActionClick = onReload,
        modifier = Modifier.fillMaxSize(),
      )

      else -> uiState.videoDetail?.let { detail ->
        movieBackgroundPoster(detail.coverUrl)
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(vertical = 100.dp),
          verticalArrangement = Arrangement.spacedBy(25.dp),
        ) {
          item(key = "Header") { headerContent(detail) }
          detail.ids.tmdbId?.let {
            item(key = "Casts") { castsContent(it) }
            item(key = "Similar") { similarContent(it) }
          }
          item(key = "Info") { informationContent(detail) }
        }
      }
    }
  }
}

@Composable
private fun DetailContentLoading() {
  Box(modifier = Modifier.fillMaxSize()) {
    PosterLoadingPlaceholder(modifier = Modifier.fillMaxSize())
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(vertical = 100.dp),
      verticalArrangement = Arrangement.spacedBy(35.dp),
    ) {
      item {
        DetailHeaderLoadingPlaceholder(
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 50.dp),
        )
      }
      item { CastRowLoading() }
    }
  }
}
