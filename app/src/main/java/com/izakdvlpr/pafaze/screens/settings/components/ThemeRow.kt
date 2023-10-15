package com.izakdvlpr.pafaze.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.utils.noRippleClickable
import com.izakdvlpr.pafaze.viewmodels.ColorMode

@Composable
fun ThemeRow(
  theme: ColorMode,
  index: Int,
  lastIndex: Int,
  isSelected: Boolean,
  onClick: () -> Unit,
) {
  val containerGap = 20.dp

  Row(
    modifier = Modifier
      .padding(
        top = if (index == 0) containerGap else 0.dp,
        bottom = if (index == lastIndex) 0.dp else containerGap
      )
      .noRippleClickable { onClick() },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy((containerGap - 10.dp) / 2)
  ) {
    RadioButton(selected = isSelected, onClick = null)

    Text(text = theme.title)
  }
}