package io.filmtime.core.designsystem

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection

operator fun PaddingValues.plus(plus: PaddingValues): PaddingValues = PaddingValues(
  start = this.calculateStartPadding(LayoutDirection.Ltr) +
    plus.calculateStartPadding(LayoutDirection.Ltr),
  top = this.calculateTopPadding() + plus.calculateTopPadding(),
  end = this.calculateEndPadding(LayoutDirection.Ltr) +
    plus.calculateEndPadding(LayoutDirection.Ltr),
  bottom = this.calculateBottomPadding() + plus.calculateBottomPadding(),
)
