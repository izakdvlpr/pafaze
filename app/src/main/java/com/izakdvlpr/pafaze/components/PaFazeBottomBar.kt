package com.izakdvlpr.pafaze.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.izakdvlpr.pafaze.navigation.NavigationRoutes

@Composable
fun PaFazeBottomBar(
  navController: NavHostController,
) {
  val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
  val currentRoute = currentBackStackEntryAsState?.destination?.route

  val navigations = listOf<NavItem>(
    NavItem(
      name = "Tarefas",
      route = NavigationRoutes.tasks,
      icon = Icons.Outlined.ReceiptLong
    ),
//    NavItem(
//      name = "Notas",
//      route = NavigationRoutes.notes,
//      icon = Icons.Outlined.Edit
//    ),
    NavItem(
      name = "Eventos",
      route = NavigationRoutes.events,
      icon = Icons.Outlined.Event
    )
  )

  NavigationBar {
    navigations.forEach { navigation ->
      NavigationBarItem(
        label = {
          Text(
            text = navigation.name,
            style = MaterialTheme.typography.bodySmall
          )
        },
        icon = {
          Icon(
            imageVector = navigation.icon,
            contentDescription = null
          )
        },
        selected = currentRoute == navigation.route,
        onClick = {
          if (currentRoute != navigation.route) {
            navController.navigate(navigation.route)
          }
        }
      )
    }
  }
}

data class NavItem(
  val name: String,
  val route: String,
  val icon: ImageVector
)