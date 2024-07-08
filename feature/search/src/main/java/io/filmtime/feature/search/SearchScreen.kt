package io.filmtime.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchResult.Person
import io.filmtime.data.model.SearchResult.TvShow
import io.filmtime.data.model.SearchResult.Video
import io.filmtime.data.model.SearchType
import io.filmtime.feature.search.components.SearchTypeChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
) {
  var text by remember { mutableStateOf("") }
  var active by remember { mutableStateOf(false) }
  var searchType by remember { mutableStateOf(SearchType.All) }
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusManager = LocalFocusManager.current

  val viewModel = hiltViewModel<SearchViewModel>()
  val state by viewModel.state.collectAsState()

  Scaffold(
    topBar = {
      SearchBar(
        query = text,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        onQueryChange = { text = it },
        onSearch = {
          active = false
          keyboardController?.hide()
          focusManager.clearFocus()
          viewModel.search(it, searchType)
        },
        active = false,
        onActiveChange = {},
        placeholder = { Text("Search") },
        leadingIcon = {
          Icon(Icons.Rounded.Search, contentDescription = "search")
        },
        trailingIcon = {
          if (active) {
            Icon(
              Icons.Rounded.Close,
              modifier = Modifier.clickable {
                if (text.isEmpty()) {
                  active = false
                }
                text = ""
              },
              contentDescription = "close",
            )
          }
        },
        content = {},
      )
    },
  ) {
    Column(
      modifier = Modifier.padding(it),
      verticalArrangement = Arrangement.Top,
    ) {
      SearchTypeChip(
        selectedSearchType = searchType,
        onChange = { type ->
          searchType = type
          if (text.isNotEmpty()) {
            viewModel.search(text, type)
          }
        },
      )
      Column(
        modifier = Modifier.fillMaxSize(),
      ) {
        if (state.loading) {
          Box(
            modifier = Modifier.fillMaxHeight(),
          ) {
            CircularProgressIndicator()
          }
        } else if (state.error != null) {
          Text(state.error!!)
        } else {
          if (state.hasResult == false) {
            Text("No result")
          } else {
            MovieSearchListGrid(pagedList = state.data)
//            when(searchType) {
//              All -> TODO()
//              Movie, Show -> MovieSearchListGrid(pagedList = state.data.map {  })
//            }
//            LazyColumn(
//              modifier = Modifier
//                .padding(16.dp),
//            ) {
//              items(state.data) { item ->
//                Card(
//                  modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                      when (item) {
//                        is Person -> TODO()
//                        is TvShow -> item.item.ids.tmdbId?.let { it1 -> onShowClick(it1) }
//                        is Video -> item.item.ids.tmdbId?.let { it1 -> onMovieClick(it1) }
//                      }
//                    },
//                ) {
//                  when (item) {
//                    is Person -> Text(item.name)
//                    is TvShow, is Video -> Text(item.item.title)
//                  }
// //                  Row {
// //                    AsyncImage(
// //                      model = item.posterUrl,
// //                      contentDescription = item.title,
// //                      modifier = Modifier.size(80.dp),
// //                      contentScale = ContentScale.Crop,
// //                    )
// //                    Spacer(modifier = Modifier.width(8.dp))
// //                    Text(item.title)
// //                  }
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//              }
//            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieSearchListGrid(
  modifier: Modifier = Modifier,
  pagedList: List<SearchResult>,
) {
  LazyVerticalGrid(
    columns = Adaptive(100.dp),
    modifier = Modifier,
    contentPadding = PaddingValues(16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(
      pagedList.count(),
//      key = { index -> pagedList[index]?.ids?.tmdbId ?: index },
    ) { index ->
      val videoThumbnail = pagedList[index]
      SearchThumbnailCardContent(videoThumbnail = videoThumbnail)
    }
  }
}

@Composable
private fun SearchThumbnailCardContent(
  videoThumbnail: SearchResult,
) {
  when (videoThumbnail) {
    is Person -> Column(
      verticalArrangement = Arrangement.Center,
    ) {
      AsyncImage(
        model = videoThumbnail.imageUrl,
        contentDescription = videoThumbnail.name,
        modifier = Modifier.clip(CircleShape),
        contentScale = ContentScale.FillWidth,
      )
      Text(videoThumbnail.name)
    }
    is TvShow -> AsyncImage(
      model = videoThumbnail.item.posterUrl,
      contentDescription = videoThumbnail.item.title,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )

    is Video -> AsyncImage(
      model = videoThumbnail.item.posterUrl,
      contentDescription = videoThumbnail.item.title,
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop,
    )
  }
}
