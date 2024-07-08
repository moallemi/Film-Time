package io.filmtime.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.filmtime.core.ui.common.componnents.VideoThumbnailCard
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
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
      ) {
        if (state.loading) {
          CircularProgressIndicator()
        } else if (state.error != null) {
          Text(state.error!!)
        } else {
          if (state.hasResult == false) {
            Text("No result")
          } else {
            SearchListGrid(
              pagedList = state.data,
              onTap = { item ->
                when (item) {
                  is Person -> {}
                  is TvShow -> {
                    item.item.ids.tmdbId?.let(onShowClick)
                  }

                  is Video -> {
                    item.item.ids.tmdbId?.let(onMovieClick)
                  }
                }
              },
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchListGrid(
  pagedList: List<SearchResult>,
  onTap: ((SearchResult) -> Unit)?,
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
      val item = pagedList[index]
      SearchThumbnailCardContent(item = item, onTap = onTap)
    }
  }
}

@Composable
private fun SearchThumbnailCardContent(
  item: SearchResult,
  onTap: ((SearchResult) -> Unit)? = null,
) {
  when (item) {
    is Person -> Column(
      modifier = Modifier
        .fillMaxSize()
        .wrapContentHeight()
        .clickable {
          onTap?.invoke(item)
        }
        .padding(horizontal = 6.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      AsyncImage(
        modifier = Modifier
          .size(100.dp)
          .clip(CircleShape) // clip to the circle shape
          .border(1.dp, Color.Transparent, CircleShape),
        contentScale = ContentScale.Crop,
        model = item.imageUrl,
        contentDescription = item.name,
        alignment = Alignment.Center,
      )
      Text(
        text = item.name,
        style = TextStyle(
          fontWeight = FontWeight.Light,
          fontSize = 16.sp,
          color = Color.Black,
        ),
        modifier = Modifier.padding(vertical = 4.dp),
      )
    }

    is TvShow -> VideoThumbnailCard(
      imageUrl = item.item.posterUrl,
      title = item.item.title,
      onClick = {
        onTap?.invoke(item)
      },
    )

    is Video -> VideoThumbnailCard(
      imageUrl = item.item.posterUrl,
      title = item.item.title,
      onClick = {
        onTap?.invoke(item)
      },
    )
  }
}
