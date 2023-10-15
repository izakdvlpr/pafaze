package com.izakdvlpr.pafaze.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.utils.noRippleClickable

@Composable
fun SettingsRow(
  title: String,
  value: String,
  icon: ImageVector,
  onClick: () -> Unit,
) {
  val containerGap = 20.dp

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .noRippleClickable { onClick() },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(containerGap)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = "Theme"
    )

    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge
      )

      Text(
        text = value,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodySmall
      )
    }
  }
}