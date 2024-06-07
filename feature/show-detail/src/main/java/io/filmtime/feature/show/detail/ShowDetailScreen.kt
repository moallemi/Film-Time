package io.filmtime.feature.show.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.filmtime.core.ui.common.componnents.CreditRowItem
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.core.ui.common.componnents.LoadingCastSectionRow
import io.filmtime.core.ui.common.componnents.VideoThumbnailCard
import io.filmtime.data.model.VideoDetail

@Composable
fun ShowDetailScreen(
  viewModel: ShowDetailViewModel,
  onCastItemClick: (Long) -> Unit,
  onSimilarClick: (Int) -> Unit,
  onBackPressed: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val creditState by viewModel.creditState.collectAsStateWithLifecycle()
  val similarState by viewModel.similarState.collectAsStateWithLifecycle()
  val videoDetail = state.videoDetail

  if (state.isLoading) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.fillMaxSize(),
    ) {
      CircularProgressIndicator(
        modifier = Modifier.wrapContentSize(unbounded = true),
      )
    }
  } else if (state.error != null) {
    ErrorContent(
      uiMessage = state.error!!,
      onRetryClick = viewModel::load,
    )
  } else if (videoDetail != null) {
    ShowDetailContent(
      videoDetail,
      creditState,
      similarState,
      onSimilarClick,
      onBackPressed,
    )
  }
}

@Composable
fun ShowDetailContent(
  videoDetail: VideoDetail,
  creditState: ShowDetailCreditState,
  similarState: ShowDetailSimilarState,
  onSimilarItemClick: (Int) -> Unit,
  onBackPressed: () -> Unit,
) {
  val scrollState = rememberScrollState()
  var sizeImage by remember { mutableStateOf(IntSize.Zero) }
  val gradient = Brush.verticalGradient(
    colors = listOf(Color.Transparent, Color.Black),
    startY = sizeImage.height.toFloat() / 2, // 1/3
    endY = sizeImage.height.toFloat(),
  )
  Column(
    modifier = Modifier
      .verticalScroll(scrollState),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Box(
      modifier = Modifier
        .height(400.dp),
    ) {
      AsyncImage(
        modifier = Modifier
          .onGloballyPositioned {
            sizeImage = it.size
          }
          .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        model = videoDetail.coverUrl,
        contentDescription = null,
        alignment = Alignment.BottomCenter,
      )
      Box(
        modifier = Modifier
          .matchParentSize()
          .background(gradient),
      )
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(start = 16.dp, bottom = 16.dp),
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            style = TextStyle(
              fontWeight = FontWeight.Bold,
              fontSize = 30.sp,
              color = Color.White,
            ),
            text = videoDetail.title,
          )
        }

        Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
          Text(
            modifier = Modifier.padding(start = 16.dp),
            style = TextStyle(
              fontWeight = FontWeight.Light,
              fontSize = 15.sp,
              color = Color.White,
            ),
            text = videoDetail.runtime.toString(),
          )
          Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = TextStyle(
              fontWeight = FontWeight.Light,
              fontSize = 15.sp,
              color = Color.White,
            ),
            text = videoDetail.releaseDate,
          )
          Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
              modifier = Modifier.size(20.dp),
              progress = { videoDetail.voteAverage },
              color = Color(videoDetail.voteColor),
            )
            Text(
              modifier = Modifier.padding(horizontal = 2.dp),
              style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 15.sp,
                color = Color.White,
              ),
              text = String.format("%.1f", videoDetail.voteAverage.times(10)),
            )
          }
        }
      }

      IconButton(onClick = onBackPressed) {
        Icon(Filled.ArrowBack, contentDescription = "back")
      }
    }

    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      text = videoDetail.description,
    )
    Text(
      modifier = Modifier.padding(horizontal = 16.dp),
      style = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.Black,
      ),
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

    if (similarState.isLoading) {
      Box {} // Fixme: Add shimmer
    } else if (similarState.videoItems.isNotEmpty()) {
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        style = TextStyle(
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = Color.Black,
        ),
        text = "Similar",
      )
      LazyRow(
        modifier = Modifier
          .height(200.dp)
          .padding(bottom = 6.dp)
          .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(similarState.videoItems) { item ->
          VideoThumbnailCard(
            modifier = Modifier,
            videoThumbnail = item,
            onClick = {
              item.ids.tmdbId?.let {
                onSimilarItemClick(it)
              } ?: run {
              }
            },
          )
        }
      }
    }
  }
}
