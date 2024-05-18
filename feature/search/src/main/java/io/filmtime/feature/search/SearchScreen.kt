package io.filmtime.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.data.model.VideoType.Show
import io.filmtime.feature.search.components.SearchTypeChip

enum class SearchType {
  All,
  Movie,
  Show,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
  onMovieClick: (tmdbId: Int) -> Unit,
  onShowClick: (tmdbId: Int) -> Unit,
) {
  var text by remember { mutableStateOf("") }
  var active by remember { mutableStateOf(false) }
  var searchType by remember { mutableStateOf(SearchType.All) }
  val history = remember { mutableStateListOf<String>() }

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
          history.add(it)
          viewModel.search(it, searchType)
        },
        active = active,
        onActiveChange = { active = it },
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
      ) {
        history.forEach {
          Text(it)
        }
      }
    },
  ) {
    Column(
      modifier = Modifier.padding(it),
    ) {
      SearchTypeChip(selectedSearchType = searchType, onChange = { type -> searchType = type })
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        if (state.loading) {
          CircularProgressIndicator()
        } else {
          if (state.hasResult == false) {
            Text("No result")
          } else {
            LazyColumn(
              modifier = Modifier
                .padding(16.dp),
            ) {
              items(state.data) { item ->
                Card(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                      item.ids.tmdbId?.let { id ->
                        when (item.type) {
                          Movie -> onMovieClick(id)
                          Show -> onShowClick(id)
                        }
                      }
                    },
                ) {
                  Row {
                    AsyncImage(
                      model = item.posterUrl,
                      contentDescription = item.title,
                      modifier = Modifier.size(80.dp),
                      contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(item.title)
                  }
                }
                Spacer(modifier = Modifier.height(8.dp))
              }
            }
          }
        }
      }
    }
  }
}
