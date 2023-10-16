package com.izakdvlpr.pafaze.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.izakdvlpr.pafaze.viewmodels.ColorMode
import com.izakdvlpr.pafaze.viewmodels.ThemeState
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@Composable
fun ThemeDialog(
  themeState: ThemeState,
  themeViewModel: ThemeViewModel,
  onCloseThemeDialog: () -> Unit,
) {
  val cardHeight = 225.dp
  val cardPadding = 30.dp

  Dialog(onDismissRequest = { onCloseThemeDialog() }) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(cardHeight),
      shape = MaterialTheme.shapes.large,
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(cardPadding)
      ) {
        Text(
          text = "Tema",
          color = MaterialTheme.colorScheme.primary,
          style = MaterialTheme.typography.titleMedium
        )

        val themes = listOf(ColorMode.NORD)

        themes.forEachIndexed { index, theme ->
          ThemeRow(
            theme = theme,
            index = index,
            lastIndex = themes.lastIndex,
            isSelected = themeState.colorMode == theme,
            onClick = {
              themeViewModel.setColorMode(theme)

              onCloseThemeDialog()
            }
          )
        }
      }
    }
  }
}