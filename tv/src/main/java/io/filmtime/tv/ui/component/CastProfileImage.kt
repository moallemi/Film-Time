package io.filmtime.tv.ui.component

import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import coil.compose.SubcomposeAsyncImage
import io.filmtime.tv.R

@Composable
fun CastProfileImage(
  modifier: Modifier = Modifier,
  imageUrl: String,
) {
  SubcomposeAsyncImage(
    loading = { ProfileLoadingPlaceholder() },
    error = { ProfileErrorPlaceholder() },
    modifier = modifier,
    contentScale = ContentScale.Crop,
    model = imageUrl,
    contentDescription = stringResource(R.string.cd_cast_profile),
  )
}

@Composable
private fun ProfileLoadingPlaceholder() {
  val infiniteTransition = rememberInfiniteTransition(label = "Profile loading transition")
  val alphaAnimation by infiniteTransition.animateFloat(
    initialValue = .1f,
    targetValue = .5f,
    animationSpec = infiniteRepeatable(
      repeatMode = Reverse,
      animation = tween(500),
    ),
    label = "Placeholder fadeIn/fadeOut animation",
  )
  Icon(
    modifier = Modifier
      .size(24.dp)
      .graphicsLayer {
        alpha = alphaAnimation
      },
    imageVector = Icons.Rounded.Person,
    contentDescription = null,
  )
}

@Composable
private fun ProfileErrorPlaceholder() {
  Icon(
    imageVector = Icons.Rounded.Person,
    contentDescription = null,
  )
}
