package io.filmtime.feature.player

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import io.filmtime.data.model.StreamInfo

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(streamInfo: StreamInfo) {
  val context = LocalContext.current

  val exoPlayer = remember {
    val dataSourceFactory = DefaultHttpDataSource.Factory().apply {
      if (streamInfo.headers.isNotEmpty()) {
        setDefaultRequestProperties(streamInfo.headers)
      }
    }

    val subtitleConfigs = streamInfo.subtitles.map { subtitle ->
      MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitle.url))
        .setMimeType(inferSubtitleMimeType(subtitle.url))
        .setLanguage(subtitle.language)
        .setLabel(subtitle.label ?: subtitle.language)
        .build()
    }

    val mediaItem = MediaItem.Builder()
      .setUri(streamInfo.url)
      .setSubtitleConfigurations(subtitleConfigs)
      .build()

    val mediaSource = when (streamInfo.streamType) {
      "hls" -> HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
      "dash" -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
      else -> ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
    }

    ExoPlayer.Builder(context).build().apply {
      setMediaSource(mediaSource)
      playWhenReady = true
      prepare()
    }
  }

  val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

  DisposableEffect(key1 = lifecycleOwner) {
    val observer = LifecycleEventObserver { owner: LifecycleOwner, event: Lifecycle.Event ->
      when (event) {
        ON_RESUME -> {
          exoPlayer.play()
        }
        ON_PAUSE -> {
          exoPlayer.pause()
        }
        else -> Unit
      }
    }

    val lifecycle = lifecycleOwner.value.lifecycle
    lifecycle.addObserver(observer)

    onDispose {
      exoPlayer.release()
      lifecycle.removeObserver(observer)
    }
  }

  AndroidView(factory = { context ->
    PlayerView(context).apply {
      player = exoPlayer
    }
  })
}

private fun inferSubtitleMimeType(url: String): String {
  val extension = url.substringAfterLast('.', "").lowercase().substringBefore('?')
  return when (extension) {
    "vtt", "webvtt" -> MimeTypes.TEXT_VTT
    "srt" -> MimeTypes.APPLICATION_SUBRIP
    "ass", "ssa" -> MimeTypes.TEXT_SSA
    "ttml", "xml", "dfxp" -> MimeTypes.APPLICATION_TTML
    else -> MimeTypes.TEXT_VTT
  }
}
