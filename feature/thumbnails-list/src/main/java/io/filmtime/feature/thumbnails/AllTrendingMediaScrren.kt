package io.filmtime.feature.thumbnails
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType
import io.filmtime.feature.home.VideoThumbnailCard

@Composable
fun AllTrendingMedia(
  title : String,
  viewModel: AllthumbnailsViewmodel,
  onBackPressed :()->Unit,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,

) {

  val moviePagingItems = viewModel.TrendingMovies.collectAsLazyPagingItems()

  when (title){
    "Trending Movies"-> {
      AllThumbnails(pagingItems = moviePagingItems, onBackPressed = onBackPressed,
        onMovieClick =onMovieClick , onShowClick =onShowClick, title = title )
    }
    "Trending Shows"  -> {}
  }


  }

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AllThumbnails(
  title: String,
  pagingItems: LazyPagingItems<VideoThumbnail>,
  onBackPressed: () -> Unit,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = title)
        },
        navigationIcon = {
          IconButton(onClick = onBackPressed) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
          }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
      )

    },
  ) {
    val SwipeRefreshState = rememberSwipeRefreshState(isRefreshing =false)
    SwipeRefresh(state = SwipeRefreshState, onRefresh = { pagingItems.refresh()
      SwipeRefreshState.isRefreshing = true
    }){
      Box(modifier = Modifier
        .padding(it)
        .fillMaxSize()){

        if (pagingItems.loadState.refresh is LoadState.Loading) {
          CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
          )

        } else if (pagingItems.loadState.refresh is LoadState.Error){
          Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Text(text = "Error Occoured ")
            Text(text = "Retry", modifier= Modifier.clickable { pagingItems.refresh() })
          }

        } else{
          SwipeRefreshState.isRefreshing = false
          LazyVerticalGrid(columns = GridCells.FixedSize(137.dp),
            modifier = Modifier.padding(top=2.dp, start = 10.dp)) {
            items(
              pagingItems.itemCount
            ) {
              val videoThumbnail = pagingItems[it]
              if (videoThumbnail != null) {
                VideoThumbnailCard(
                  videoThumbnail = videoThumbnail,
                  onClick = {

                    videoThumbnail.ids.tmdbId?.let {
                      when (videoThumbnail.type) {
                        VideoType.Movie -> onMovieClick(it)
                        VideoType.Show -> onShowClick(it)
                      }
                    } ?: run {
                      Log.e("HomeScreen", "tmdbId is null")
                    }

                  }
                )

              }
            }


            if (pagingItems.loadState.append is LoadState.Loading) {
              item {
                Box(modifier = Modifier.fillMaxSize()){
                  CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                  )
                }
              }
            }
            if (pagingItems.loadState.append is LoadState.Error) {
              item {
                ErrorFooter {
                  pagingItems.retry()
                }
              }
            }
            if (pagingItems.loadState.prepend is LoadState.Loading) {
              item {
                Box(modifier = Modifier.fillMaxSize()){
                  CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                  )
                }
              }
            }
            if (pagingItems.loadState.prepend is LoadState.Error) {
              item {
                ErrorHeader {
                  pagingItems.retry()
                }
              }
            }
          }
        }
      }
    }
  }
}


@Composable
fun ErrorHeader(retry: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = "Retry",
      modifier = Modifier
        .clickable { retry.invoke() },
      style = MaterialTheme.typography.bodyMedium
    )
  }
}
@Composable
fun ErrorFooter(retry: () -> Unit) {
  Column(modifier = Modifier
    .padding(8.dp)
    .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center) {
      Image(imageVector = Icons.Default.Warning, contentDescription = null, modifier = Modifier.padding(8.dp))
        Text(
          text = "Retry",
          modifier = Modifier
            .clickable { retry.invoke() },
          style = MaterialTheme.typography.bodyMedium
        )
    }

  }




