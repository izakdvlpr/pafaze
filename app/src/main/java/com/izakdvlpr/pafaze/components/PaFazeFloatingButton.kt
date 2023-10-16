package com.izakdvlpr.pafaze.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PaFazeFloatingButton(
  title: String,
  onClick: () -> Unit,
) {
  ExtendedFloatingActionButton(
    containerColor = MaterialTheme.colorScheme.primary,
    onClick = { onClick() }
  ) {
    Icon(
      imageVector = Icons.Outlined.AddBox,
      contentDescription = null
    )

    Spacer(modifier = Modifier.width(10.dp))

    Text(
      text = title,
      style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Start)
    )
  }
}