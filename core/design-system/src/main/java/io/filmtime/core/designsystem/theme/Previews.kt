package io.filmtime.core.designsystem.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Phone - Small", group = "Device", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Preview(name = "Phone - Medium", group = "Device", device = "spec:width=411dp,height=891dp")
@Preview(name = "Phone - Pixel 6 Pro", group = "Device", device = "id:pixel_6_pro")
annotation class DevicePreviews

@Preview(
  name = "Light theme",
  uiMode = Configuration.UI_MODE_NIGHT_NO,
  group = "Theme",
  showBackground = true,
  backgroundColor = 0xFFFFFF,
)
@Preview(
  name = "Night/Dark theme",
  uiMode = Configuration.UI_MODE_NIGHT_YES,
  group = "Theme",
  showBackground = true,
  backgroundColor = 0x000000,
)
annotation class ThemePreviews
