package io.filmtime.feature.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.filmtime.core.ui.common.componnents.PersonSearchCard
import io.filmtime.core.ui.common.componnents.VideoSearchCard
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchResult.Person
import io.filmtime.data.model.SearchResult.TvShow
import io.filmtime.data.model.SearchResult.Video
import io.filmtime.data.model.SearchType
import io.filmtime.feature.search.components.SearchTypeChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
  viewModel: SearchViewModel,
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
  onPersonClick: (personId: Int) -> Unit,
) {
  var text by rememberSaveable { mutableStateOf("") }
  var searchType by rememberSaveable { mutableStateOf(SearchType.All) }
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusManager = LocalFocusManager.current

  Scaffold(
    topBar = {
      SearchBar(
        query = text,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        onQueryChange = { text = it },
        onSearch = {
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
        val result = viewModel.state.collectAsLazyPagingItems()
        SearchListGrid(
          pagedList = result,
          onTap = { item ->
            when (item) {
              is Person -> {
              }

              is TvShow -> {
                item.item.ids.tmdbId?.let(onShowClick)
              }

              is Video -> {
                item.item.ids.tmdbId?.let(onMovieClick)
              }
            }
          },
        )
//        if (state.loading) {
//          CircularProgressIndicator()
//        } else if (state.error != null) {
//          Text(state.error!!)
//        } else {
//          if (state.hasResult == false) {
//            Text("No result")
//          } else {
//            SearchListGrid(
//              pagedList = state.data,
//              onTap = { item ->
//                when (item) {
//                  is Person -> {}
//                  is TvShow -> {
//                    item.item.ids.tmdbId?.let(onShowClick)
//                  }
//
//                  is Video -> {
//                    item.item.ids.tmdbId?.let(onMovieClick)
//                  }
//                }
//              },
//            )
//          }
//        }
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchListGrid(
  pagedList: LazyPagingItems<SearchResult>,
  onTap: (SearchResult) -> Unit,
) {
  LazyVerticalGrid(
    columns = Adaptive(100.dp),
    modifier = Modifier,
    contentPadding = PaddingValues(16.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(
      pagedList.itemCount,
    ) { index ->
      val item = pagedList[index]
      SearchThumbnailCardContent(
        modifier = Modifier
          .animateItemPlacement()
          .fillMaxWidth()
          .aspectRatio(2 / 3f),
        item = item!!,
        onTap = onTap,
      )
    }
  }
}

@Composable
private fun SearchThumbnailCardContent(
  modifier: Modifier = Modifier,
  item: SearchResult,
  onTap: (SearchResult) -> Unit,
) {
  Box(modifier = modifier) {
    when (item) {
      is Person -> PersonSearchCard(item = item)

      is TvShow -> VideoSearchCard(
        imageUrl = item.item.posterUrl,
        title = item.item.title,
        onClick = {
          onTap(item)
        },
      )

      is Video -> VideoSearchCard(
        imageUrl = item.item.posterUrl,
        title = item.item.title,
        onClick = {
          onTap(item)
        },
      )
    }
  }
}
