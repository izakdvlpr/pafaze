package com.izakdvlpr.pafaze.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.izakdvlpr.pafaze.screens.home.HomeScreen
import com.izakdvlpr.pafaze.screens.settings.SettingsScreen
import com.izakdvlpr.pafaze.viewmodels.HomeViewModel
import com.izakdvlpr.pafaze.viewmodels.SettingsViewModel
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@Composable
fun NavigationController(themeViewModel: ThemeViewModel) {
  val navController = rememberNavController()

  val homeViewModel: HomeViewModel = hiltViewModel()
  val settingsViewModel: SettingsViewModel = hiltViewModel()

  NavHost(
    navController = navController,
    startDestination = NavigationRoutes.home,
  ) {
    composable(route = NavigationRoutes.home) {
      HomeScreen(
        navController = navController,
        homeViewModel = homeViewModel
      )
    }

    composable(route = NavigationRoutes.settings) {
      SettingsScreen(
        navController = navController,
        themeViewModel = themeViewModel,
        settingsViewModel = settingsViewModel,
      )
    }
  }
}