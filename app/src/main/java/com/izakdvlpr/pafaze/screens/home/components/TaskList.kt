package com.izakdvlpr.pafaze.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.viewmodels.HomeState
import com.izakdvlpr.pafaze.viewmodels.HomeViewModel

@Composable
fun TaskList(
  homeState: HomeState,
  homeViewModel: HomeViewModel
) {
  val containerGap = 20.dp

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(containerGap / 2)
  ) {
//    homeState.tasks.sortBy { it.done }

    homeState.tasks
      .filter {
        it.title.lowercase() == homeState.search || it.title.contains(homeState.search)
      }
      .forEach { task ->
        TaskCard(
          task = task,
          homeViewModel = homeViewModel
        )
      }
  }
}