package com.izakdvlpr.pafaze.components.topbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaFazeTopBarBack(
  onActionBack: () -> Unit,
  onActionReset: () -> Unit,
) {
  CenterAlignedTopAppBar(
    navigationIcon = {
      IconButton(onClick = { onActionBack() }) {
        Icon(
          imageVector = Icons.Filled.ArrowBack,
          contentDescription = "Back"
        )
      }
    },
    title = {
      Text(
        text = "Configurações",
        style = MaterialTheme.typography.bodyLarge
      )
    },
    actions = {
      IconButton(onClick = { onActionReset() }) {
        Icon(
          imageVector = Icons.Outlined.RestartAlt,
          contentDescription = "Reset"
        )
      }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      titleContentColor = MaterialTheme.colorScheme.primary,
      navigationIconContentColor = Color.White,
      actionIconContentColor = Color.White,
    ),
  )
}