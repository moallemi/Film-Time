package io.filmtime.feature.show.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.UiMessage
import io.filmtime.core.ui.common.componnents.ErrorContent
import io.filmtime.core.ui.common.componnents.Select
import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.Preview

@Composable
internal fun SeasonsSection(
  isLoading: Boolean,
  seasons: Map<Int, List<EpisodeThumbnail>>,
  seasonsNumber: Int,
  modifier: Modifier = Modifier,
  error: UiMessage? = null,
  onRetryClick: () -> Unit,
) {
  var selectedSeason by remember { mutableIntStateOf(1) }

  Column(
    modifier = modifier,
  ) {
    if (seasonsNumber > 1) {
      val items = List(seasonsNumber) { it + 1 }
      Select(
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .padding(bottom = 8.dp)
          .width(150.dp),
        options = items,
        itemText = { "Season $it" },
        selectedItem = selectedSeason,
        onSelect = { selectedSeason = it },
      )
    } else {
      Text(
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .padding(bottom = 8.dp),
        style = MaterialTheme.typography.titleMedium,
        text = "Season 1",
      )
    }

    if (isLoading) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
      ) {
        items(2) { index ->
          EpisodeThumbnailCard(
            modifier = Modifier
              .width(300.dp),
            episodeThumbnail = EpisodeThumbnail.Preview,
            placeHolderVisible = true,
          )
        }
      }
    } else if (error != null) {
      ErrorContent(
        uiMessage = error,
        onRetryClick = onRetryClick,
        showGraphicalError = false,
      )
    } else if (seasons.isNotEmpty()) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
      ) {
        items(seasons[selectedSeason].orEmpty()) { item ->
          EpisodeThumbnailCard(
            modifier = Modifier
              .width(300.dp),
            episodeThumbnail = item,
          )
        }
      }
    }
  }
}

@ThemePreviews
@Composable
private fun SeasonsSectionPreview() {
  PreviewFilmTimeTheme {
    Column {
      SeasonsSection(
        isLoading = false,
        seasonsNumber = 1,
        seasons = mapOf(
          1 to listOf(EpisodeThumbnail.Preview, EpisodeThumbnail.Preview),
        ),
        error = null,
        onRetryClick = {},
      )

      Spacer(modifier = Modifier.padding(16.dp))

      SeasonsSection(
        isLoading = false,
        seasonsNumber = 3,
        seasons = mapOf(
          1 to listOf(EpisodeThumbnail.Preview, EpisodeThumbnail.Preview),
          2 to listOf(EpisodeThumbnail.Preview, EpisodeThumbnail.Preview),
          3 to listOf(EpisodeThumbnail.Preview, EpisodeThumbnail.Preview),
        ),
        error = null,
        onRetryClick = {},
      )
    }
  }
}
