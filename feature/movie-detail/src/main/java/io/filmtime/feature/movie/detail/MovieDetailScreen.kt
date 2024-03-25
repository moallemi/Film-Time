package io.filmtime.feature.movie.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import io.filmtime.core.ui.common.componnents.LoadingCastSectionRow
import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.VideoDetail

@Composable
fun MovieDetailScreen(
  viewModel: MovieDetailViewModel,
  onStreamReady: (String) -> Unit,
  onBackPressed: () -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val creditState by viewModel.creditState.collectAsStateWithLifecycle()
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
    ShowError(error = state.error!!, message = state.message!!, onRefresh = viewModel::load)
  } else if (videoDetail != null) {
    MovieDetailContent(
      videoDetail = videoDetail,
      state = state,
      creditState = creditState,
      onBackPressed = onBackPressed,
      onPlayPressed = viewModel::loadStreamInfo,
      onAddToHistoryPressed = viewModel::addItemToHistory,
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
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,

    ) {
    LottieAnimation(
      modifier = Modifier.scale(0.8f),
      composition = composition,
    )
    Spacer(modifier = Modifier.size(8.dp))
    Text(
      text = message,
      textAlign = TextAlign.Center,
      style = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
      ),
    )

    Spacer(modifier = Modifier.size(60.dp))
    Button(
      onClick = onRefresh,
    ) {
      Text(text = stringResource(R.string.refresh))
    }
  }
}

@Composable
fun MovieDetailContent(
  videoDetail: VideoDetail,
  state: MovieDetailState,
  onBackPressed: () -> Unit,
  onPlayPressed: () -> Unit,
  onAddToHistoryPressed: () -> Unit,
  creditState: MovieDetailCreditState,
) {
  Column(
    modifier = Modifier.verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Box(
      modifier = Modifier
        .height(500.dp),
    ) {
      AsyncImage(
        modifier = Modifier
          .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        model = videoDetail.coverUrl,
        contentDescription = null,
        alignment = Alignment.BottomCenter,
      )
      Column(modifier = Modifier.align(Alignment.BottomStart)) {
        Text(
          modifier = Modifier.padding(horizontal = 16.dp),
          style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.White,
          ),
          text = videoDetail.title,
        )
        Row(horizontalArrangement = Arrangement.SpaceAround) {
          Text(
            modifier = Modifier.padding(horizontal = 16.dp),
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
        }
        IconButton(onClick = onPlayPressed) {
          Icon(Icons.Outlined.PlayArrow, contentDescription = "play")
        }
      }

      IconButton(onClick = onBackPressed) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "back")
      }
    }

    Text(
      modifier = Modifier.padding(horizontal = 16.dp),

      text = videoDetail.description,
    )

    Button(
      modifier = Modifier
        .padding(horizontal = 16.dp),
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
    videoDetail.isWatched?.let {
      when (it) {
        true -> OutlinedButton(
          modifier = Modifier
            .padding(horizontal = 16.dp),
          onClick = {},
        ) {
          Icon(
            Icons.Filled.Check,
            contentDescription = "",
            modifier = Modifier.size(20.dp),
          )
          Spacer(modifier = Modifier.size(8.dp))
          Text("Added to history")
        }

        false -> {
          Button(
            modifier = Modifier
              .padding(horizontal = 16.dp),
            onClick = onAddToHistoryPressed,
          ) {
            Text("Add to history")
          }
        }
      }
    }
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
//    Row(
//      modifier = Modifier.padding(horizontal = 16.dp),
//      horizontalArrangement = Arrangement.spacedBy(16.dp),
//    ) {
//      Text(text = "Year: ${videoDetail.year}")
//      Text(text = "Original language: ${videoDetail.originalLanguage}")
//      Text(text = videoDetail.spokenLanguages.joinToString(", "))
//    }
//    Text(
//      modifier = Modifier.padding(start = 16.dp),
//      text = "Genres",
//    )
//    Text(
//      modifier = Modifier.padding(horizontal = 16.dp),
//      text = videoDetail.genres.joinToString(", "),
//    )
  }
}

@Composable
fun CreditRowItem(item: CreditItem) {
  Log.d("tag", item.name)
  Column(
    modifier = Modifier
      .fillMaxWidth().wrapContentHeight()
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
      style = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
        color = Color.Black,
      ),
      modifier = Modifier.padding(vertical = 4.dp)
    )
  }
}


