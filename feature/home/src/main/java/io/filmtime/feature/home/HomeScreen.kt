package io.filmtime.feature.home
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType

@Composable
fun HomeScreen(
  viewModel: HomeViewModel,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  if (state.isLoading) {
    LoadingVideoSectionRow(numberOfSections = 2)
  } else if (state.error != null) {
    when (state.error) {
      is GeneralError.NetworkError -> {
        NetworkErroScreen() {
          viewModel.retry()
        }
      }
      is GeneralError.ApiError -> {
        ApiErrorScreen {
          viewModel.retry()
        }
      }
      else -> {
        UnknownErrorScreen()
      }
    }
  } else {
    LazyColumn(
      contentPadding = PaddingValues(top = 16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(state.videoSections) { videoSection ->
        VideoSectionRow(
          title = videoSection.title,
          items = videoSection.items,
          onMovieClick = onMovieClick,
          onShowClick = onShowClick,
        )
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoSectionRow(
  title: String,
  items: List<VideoThumbnail>,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
) {
  Column {
    Text(
      modifier = Modifier
        .padding(start = 16.dp),
      text = title,
      style = MaterialTheme.typography.titleMedium,
    )
    LazyRow(
      modifier = Modifier
        .height(200.dp)
        .fillMaxWidth(),
      contentPadding = PaddingValues(16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      items(items) { item ->
        VideoThumbnailCard(
          modifier = Modifier
            .testTag("discover_carousel_item")
            .animateItemPlacement()
            .fillParentMaxHeight()
            .aspectRatio(2 / 3f),
          videoThumbnail = item,
          onClick = {
            item.ids.tmdbId?.let {
              when (item.type) {
                VideoType.Movie -> onMovieClick(it)
                VideoType.Show -> onShowClick(it)
              }
            } ?: run {
              Log.e("HomeScreen", "tmdbId is null")
            }
          },
        )
      }
    }
  }
}

@Composable
fun UnknownErrorScreen() {
  Text(
    text = stringResource(R.string.unknown_error),
    modifier = Modifier
      .fillMaxSize()
      .padding(start = 20.dp),
    textAlign = TextAlign.Center,
  )
}

@Composable
fun ApiErrorScreen(onRetry: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Image(
      painter = painterResource(id = R.drawable.apierror),
      contentDescription = "not_found",
      modifier = Modifier
        .clip(shape = CircleShape)
        .size(150.dp),

    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = stringResource(R.string.api_error),
      modifier = Modifier.padding(start = 20.dp),
    )
    Button(onClick = onRetry, modifier = Modifier.clip(RoundedCornerShape(4.dp))) {
      Text(text = "Retry")
    }
  }
}

@Composable
fun NetworkErroScreen(onRetry: () -> Unit) {
  val context = LocalContext.current
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Image(
      painter = painterResource(id = R.drawable.internet_lost),
      contentDescription = "no_connection",
      modifier = Modifier
        .clip(shape = CircleShape)
        .size(150.dp),

    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = stringResource(R.string.network_error),
      modifier = Modifier.padding(start = 20.dp),
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(onClick = {
      val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
      context.startActivity(intent)
    }) {
      Text(text = "Open Device Settings ")
    }
    Button(onClick = onRetry) {
      Text(text = "Retry")
    }
  }
}

@Composable
fun LoadingVideoSectionRow(
  numberOfSections: Int,
) {
  LazyColumn(
    contentPadding = PaddingValues(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(numberOfSections) {
      Box(
        modifier = Modifier
          .padding(start = 16.dp)
          .size(50.dp, 20.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(shimmerBrush()),
      )
      LazyRow(
        modifier = Modifier
          .height(200.dp)
          .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        items(10) {
          Box(
            modifier = Modifier
              .fillParentMaxHeight()
              .aspectRatio(2 / 3f)
              .clip(RoundedCornerShape(16.dp))
              .background(shimmerBrush()),
          )
        }
      }
    }
  }
}
