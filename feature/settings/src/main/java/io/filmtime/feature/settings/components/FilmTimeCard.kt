package io.filmtime.feature.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.feature.settings.R

@Composable
internal fun FilmTimeCard(
  modifier: Modifier = Modifier,
) {
  OutlinedCard(
    modifier = modifier,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth()
        .padding(8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        modifier = Modifier
          .padding(16.dp)
          .clip(CircleShape)
          .size(80.dp)
          .scale(1.5f)
          .background(colorResource(id = io.filmtime.core.resources.R.color.ic_launcher_background)),
        painter = painterResource(id = io.filmtime.core.resources.R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(R.string.feature_settings_trakt_logo),
        contentScale = ContentScale.Crop,
      )

      Column {
        Text(
          modifier = Modifier
            .padding(bottom = 4.dp),
          text = "Film Time",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
          ),
        )

        val context = LocalContext.current
        val versionInfo by remember {
          val packageInfo = context.packageManager.getPackageInfo("io.filmtime", 0)
          val versionName = packageInfo.versionName
          val versionCode = packageInfo.versionCode
          mutableStateOf(versionName to versionCode)
        }

        Text(
          text = stringResource(
            id = R.string.feature_settings_version_build,
            versionInfo.first,
            versionInfo.second,
          ),
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
  }
}

@ThemePreviews
@Composable
private fun SettingsScreenPreview() {
  PreviewFilmTimeTheme {
    FilmTimeCard(
      modifier = Modifier
        .padding(16.dp),
    )
  }
}
