package com.izakdvlpr.pafaze.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TaskListEmpty() {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      modifier = Modifier.size(80.dp),
      imageVector = Icons.Outlined.EditCalendar,
      contentDescription = "EditNote"
    )

    Text(
      text = "Nenhuma\ntarefa criada",
      style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
    )

    Text(
      modifier = Modifier.padding(top = 10.dp),
      text = "Clique no botão abaixo para\ncriar sua primeira tarefa!",
      color = MaterialTheme.colorScheme.primary,
      style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
    )
  }
}