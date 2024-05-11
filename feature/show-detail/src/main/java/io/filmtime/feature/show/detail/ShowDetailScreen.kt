package io.filmtime.feature.show.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import io.filmtime.core.ui.common.componnents.CreditRowItem
import io.filmtime.core.ui.common.componnents.LoadingCastSectionRow
import io.filmtime.core.ui.common.componnents.LoadingVideoSectionRow
import io.filmtime.core.ui.common.componnents.VideoThumbnailCard
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.VideoDetail
import io.filmtime.feature.show.detail.R.drawable

@Composable
fun ShowDetailScreen(
  viewModel: ShowDetailViewModel,
  onStreamReady: (String) -> Unit,
  onCastItemClick: (Long) -> Unit,
  onSimilarClick: (Int) -> Unit,
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
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.fillMaxSize(),
    ) {
      CircularProgressIndicator(
        modifier = Modifier.wrapContentSize(unbounded = true),
      )
    }
  } else if (state.error != null) {
    ShowError(state.error!!, state.message!!) {
      viewModel.load()
    }
  } else if (videoDetail != null) {
    ShowDetailContent(
      videoDetail,
      state,
      creditState,
      similarState,
      onSimilarClick,
      onBackPressed,
      viewModel::loadStreamInfo,
    )
  }
}

@Composable
fun ShowDetailContent(
  videoDetail: VideoDetail,
  state: ShowDetailState,
  creditState: ShowDetailCreditState,
  similarState: ShowDetailSimilarState,
  onSimilarItemClick: (Int) -> Unit,
  onBackPressed: () -> Unit,
  onPlayPressed: () -> Unit,
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
          IconButton(onClick = onPlayPressed) {
            if (state.isStreamLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = Color.White,
                strokeWidth = 2.dp,
              )
            } else {
              Icon(painter = painterResource(drawable.play_circle), contentDescription = "play", tint = Color.White)
            }
          }
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
      LoadingVideoSectionRow(numberOfSections = 10, modifier = Modifier.height(200.dp))
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
      style = MaterialTheme.typography.bodyLarge,
    )

    Spacer(modifier = Modifier.size(60.dp))
    Button(
      onClick = onRefresh,
    ) {
      Text(text = "Refresh")
    }
  }
}
