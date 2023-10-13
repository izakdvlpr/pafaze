package com.izakdvlpr.pafaze.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.izakdvlpr.pafaze.screens.*
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@Composable
fun NavigationController(themeViewModel: ThemeViewModel) {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = NavigationRoutes.home,
  ) {
    composable(route = NavigationRoutes.home) {
      HomeScreen(
        navController = navController
      )
    }

    composable(route = NavigationRoutes.settings) {
      SettingsScreen(
        navController = navController,
        themeViewModel = themeViewModel
      )
    }
  }
}