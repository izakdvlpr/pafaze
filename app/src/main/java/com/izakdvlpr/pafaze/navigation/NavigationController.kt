package com.izakdvlpr.pafaze.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.izakdvlpr.pafaze.screens.events.EventsScreen
import com.izakdvlpr.pafaze.screens.NotesScreen
import com.izakdvlpr.pafaze.screens.SplashScreen
import com.izakdvlpr.pafaze.screens.settings.SettingsScreen
import com.izakdvlpr.pafaze.screens.tasks.TasksScreen
import com.izakdvlpr.pafaze.viewmodels.EventsViewModel
import com.izakdvlpr.pafaze.viewmodels.NotesViewModel
import com.izakdvlpr.pafaze.viewmodels.SettingsViewModel
import com.izakdvlpr.pafaze.viewmodels.TasksViewModel
import com.izakdvlpr.pafaze.viewmodels.ThemeViewModel

@Composable
fun NavigationController(themeViewModel: ThemeViewModel) {
  val navController = rememberNavController()

  val tasksViewModel: TasksViewModel = hiltViewModel()
  val notesViewModel: NotesViewModel = hiltViewModel()
  val eventsViewModel: EventsViewModel = hiltViewModel()
  val settingsViewModel: SettingsViewModel = hiltViewModel()

  NavHost(
    navController = navController,
    startDestination = NavigationRoutes.splash,
    enterTransition = { fadeIn(tween(800), 0.99f) },
    exitTransition = { fadeOut(tween(800), 0.99f) },
  ) {
    composable(route = NavigationRoutes.splash) {
      SplashScreen(navController = navController)
    }

    composable(route = NavigationRoutes.tasks) {
      TasksScreen(
        navController = navController,
        tasksViewModel = tasksViewModel
      )
    }

    composable(route = NavigationRoutes.notes) {
      NotesScreen(
        navController = navController,
        notesViewModel = notesViewModel
      )
    }

    composable(route = NavigationRoutes.events) {
      EventsScreen(
        navController = navController,
        eventsViewModel = eventsViewModel
      )
    }

    composable(
      route = NavigationRoutes.settings,
      enterTransition = { slideInHorizontally(tween(800)) { fullWidth -> fullWidth } },
      exitTransition = { slideOutHorizontally(tween(800)) { fullWidth -> -fullWidth } },
      popExitTransition = { slideOutHorizontally(tween(800)) { fullWidth -> fullWidth } },
      popEnterTransition = { slideInHorizontally(tween(800)) { fullWidth -> -fullWidth } }
    ) {
      SettingsScreen(
        navController = navController,
        themeViewModel = themeViewModel,
        settingsViewModel = settingsViewModel,
      )
    }
  }
}