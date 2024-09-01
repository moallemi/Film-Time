package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.R
import io.filmtime.data.model.Preview
import io.filmtime.data.model.Ratings

@Composable
fun RatingsInfo(
  modifier: Modifier = Modifier,
  ratings: Ratings,
  onRatingClick: (String) -> Unit,
) {
  CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.onSurface,
  ) {
    Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
      ratings.imdb?.let { rate ->
        RatingCard(
          icon = R.drawable.ic_imdb,
          rate = "${rate.rating}",
          votes = ratings.imdb?.votes ?: "N/A",
          onClick = { onRatingClick(rate.link!!) },
        )
        Spacer(modifier = Modifier.width(8.dp))
      }

      ratings.rottenTomatoes?.let { rate ->
        RatingCard(
          icon = if (rate.info == "Fresh") {
            R.drawable.ic_rt_fresh
          } else {
            R.drawable.ic_rt_rotten
          },
          rate = "${ratings.rottenTomatoes?.rating}",
          votes = ratings.rottenTomatoes?.info.orEmpty(),
          onClick = { onRatingClick(rate.link!!) },
        )
        Spacer(modifier = Modifier.width(8.dp))
      }

      ratings.tmdb?.let { rate ->
        RatingCard(
          icon = R.drawable.ic_tmdb_square,
          rate = "${ratings.tmdb?.rating}",
          votes = "${ratings.tmdb?.votes}",
          onClick = { onRatingClick(rate.link!!) },
        )
        Spacer(modifier = Modifier.width(8.dp))
      }

      ratings.trakt?.let { rate ->
        RatingCard(
          icon = R.drawable.ic_trakt,
          rate = "${rate.rating}",
          votes = "${rate.votes}",
          onClick = { onRatingClick(rate.link!!) },
        )
      }
    }
  }
}

@ThemePreviews
@Composable
private fun RatingsInfoPreview() {
  PreviewFilmTimeTheme {
    RatingsInfo(
      ratings = Ratings.Preview,
      onRatingClick = {},
    )
  }
}
