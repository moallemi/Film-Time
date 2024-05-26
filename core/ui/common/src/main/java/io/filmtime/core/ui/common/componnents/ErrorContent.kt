package io.filmtime.core.ui.common.componnents

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import io.filmtime.core.ui.common.R
import io.filmtime.core.ui.common.UiMessage

@Composable
fun ErrorContent(
  message: String,
  modifier: Modifier = Modifier,
  @RawRes animationResource: Int? = null,
  onRetryClick: (() -> Unit)? = null,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    animationResource?.let {
      val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResource))
      LottieAnimation(
        modifier = Modifier
          .scale(0.7f)
          .fillMaxHeight(0.5f),
        composition = composition,
      )
    }

    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      text = message,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface,
      textAlign = TextAlign.Center,
    )
    if (onRetryClick != null) {
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedButton(onClick = onRetryClick) {
        Text(text = "Retry")
      }
    }
  }
}

@Composable
fun ErrorContent(
  uiMessage: UiMessage,
  modifier: Modifier = Modifier,
  onRetryClick: (() -> Unit)? = null,
) {
  when (uiMessage) {
    is UiMessage.Res ->
      ErrorContent(
        message = stringResource(id = uiMessage.textId),
        animationResource = uiMessage.animationResource,
        modifier = modifier,
        onRetryClick = onRetryClick,
      )

    is UiMessage.PluralRes -> {
      val context = LocalContext.current
      ErrorContent(
        message = context.resources.getQuantityString(
          uiMessage.pluralId,
          uiMessage.formatArgs.first() as Int,
          *uiMessage.formatArgs.toTypedArray(),
        ),
        animationResource = uiMessage.animationResource,
        modifier = modifier,
        onRetryClick = onRetryClick,
      )
    }

    is UiMessage.Text ->
      ErrorContent(
        message = uiMessage.text,
        animationResource = uiMessage.animationResource,
        modifier = modifier,
        onRetryClick = onRetryClick,
      )
  }
}

@ThemePreviews
@Composable
private fun ErrorPreview() {
  PreviewFilmTimeTheme {
    ErrorContent(
      uiMessage = UiMessage.Res(R.string.check_internet_connection),
    )
  }
}

@ThemePreviews
@Composable
private fun ErrorWithRetryPreview() {
  PreviewFilmTimeTheme {
    ErrorContent(
      uiMessage = UiMessage.Res(R.string.check_internet_connection),
      onRetryClick = { },
    )
  }
}

@ThemePreviews
@Composable
private fun ErrorPreviewWithAnimation() {
  PreviewFilmTimeTheme {
    ErrorContent(
      uiMessage = UiMessage.Res(
        textId = R.string.check_internet_connection,
        animationResource = R.raw.network_lost,
      ),
      onRetryClick = { },
    )
  }
}
