package com.izakdvlpr.pafaze.screens.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EventNotFound() {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      modifier = Modifier.size(80.dp),
      imageVector = Icons.Outlined.SentimentDissatisfied,
      contentDescription = null
    )

    Text(
      text = "Nenhum\nevento encontrado",
      style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
    )

    Text(
      modifier = Modifier.padding(top = 10.dp),
      text = "Certifique-se de ter colocado\nos argumentos corretamente.",
      color = MaterialTheme.colorScheme.primary,
      style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
    )
  }
}