package com.izakdvlpr.pafaze

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.izakdvlpr.pafaze.navigation.NavigationController
import com.izakdvlpr.pafaze.theme.PaFazeTheme
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val themeViewModel: ThemeViewModel = viewModel()

      PaFazeTheme(themeViewModel = themeViewModel) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          NavigationController(themeViewModel = themeViewModel)
        }
      }
    }
  }
}