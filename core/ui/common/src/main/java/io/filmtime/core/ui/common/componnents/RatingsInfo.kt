package io.filmtime.core.ui.common.componnents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
) {
  CompositionLocalProvider(
    LocalContentColor provides MaterialTheme.colorScheme.onSurface,
  ) {
    Row(
      modifier = modifier,
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
      ratings.imdb?.let {
        Image(
          modifier = Modifier.size(28.dp),
          painter = painterResource(R.drawable.ic_imdb),
          contentDescription = null,
        )
        Column {
          Text(
            text = "${ratings.imdb?.rating}",
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = ratings.imdb?.votes ?: "N/A",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
      }

      ratings.rottenTomatoes?.let {
        Image(
          modifier = Modifier.size(24.dp),
          painter = if (it.info == "Fresh") {
            painterResource(R.drawable.ic_rt_fresh)
          } else {
            painterResource(R.drawable.ic_rt_rotten)
          },
          contentDescription = null,
        )
        Column {
          Text(
            text = "${ratings.rottenTomatoes?.rating}",
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = ratings.rottenTomatoes?.info.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
      }

      ratings.tmdb?.let {
        Image(
          modifier = Modifier.size(28.dp),
          painter = painterResource(R.drawable.ic_tmdb_square),
          contentDescription = null,
        )
        Column {
          Text(
            text = "${ratings.tmdb?.rating}",
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "${ratings.tmdb?.votes}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
      }

      ratings.trakt?.let {
        Image(
          modifier = Modifier.size(36.dp),
          painter = painterResource(R.drawable.ic_trakt),
          contentDescription = null,
        )
        Column {
          Text(
            text = "${ratings.trakt?.rating}",
            fontWeight = FontWeight.Bold,
          )
          Text(
            text = "${ratings.trakt?.votes}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
          )
        }
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
    )
  }
}
