package io.filmtime.core.ui.common.componnents

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RatingCard(
  @DrawableRes icon: Int,
  rate: String,
  votes: String,
  onClick: (() -> Unit)?,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    modifier = Modifier
      .clip(RoundedCornerShape(4.dp))
      .then(
        if (onClick != null) Modifier.clickable { onClick.invoke() } else Modifier,
      )
      .padding(vertical = 2.dp, horizontal = 4.dp),
  ) {
    Image(
      modifier = Modifier.size(28.dp),
      painter = painterResource(icon),
      contentDescription = null,
    )
    Column {
      Text(
        text = rate,
        fontWeight = FontWeight.Bold,
      )
      Text(
        text = votes,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
      )
    }
  }
}
