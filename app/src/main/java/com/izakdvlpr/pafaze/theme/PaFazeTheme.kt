package com.izakdvlpr.pafaze.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@Composable
fun PaFazeTheme(
  themeViewModel: ThemeViewModel,
  content: @Composable () -> Unit
) {
  val view = LocalView.current
  val isEditMode = view.isInEditMode

  val systemUiController = rememberSystemUiController()

  val themeState = themeViewModel.state.collectAsState().value

  val colorMode = themeState.colorMode

  if (!isEditMode) {
    SideEffect {
      val statusBarColor = colorMode.schema.background
      val statusBarIconsColor = Color.Transparent
      val statusBarNavigationColor = colorMode.schema.background

      systemUiController.setSystemBarsColor(color = statusBarIconsColor)
      systemUiController.setStatusBarColor(color = statusBarColor)
      systemUiController.setNavigationBarColor(color = statusBarNavigationColor)
    }
  }

  MaterialTheme(
    colorScheme = colorMode.schema,
    typography = typographies,
    shapes = shapes,
    content = content
  )
}
