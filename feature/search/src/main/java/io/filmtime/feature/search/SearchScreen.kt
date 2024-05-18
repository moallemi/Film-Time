package io.filmtime.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
  var text by remember { mutableStateOf("") }
  var active by remember { mutableStateOf(false) }
  val history = remember { mutableStateListOf<String>() }

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
  }
}
