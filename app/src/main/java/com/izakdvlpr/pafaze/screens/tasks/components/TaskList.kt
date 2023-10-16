package com.izakdvlpr.pafaze.screens.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.viewmodels.TasksState
import com.izakdvlpr.pafaze.viewmodels.TasksViewModel

@Composable
fun TaskList(
  tasksState: TasksState,
  tasksViewModel: TasksViewModel
) {
  val containerGap = 20.dp

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(containerGap / 2)
  ) {
    tasksState.tasks
      .filter {
        it.title.lowercase() == tasksState.search || it.title.contains(tasksState.search)
      }
      .forEach { task ->
        TaskCard(
          task = task,
          tasksViewModel = tasksViewModel
        )
      }
  }
}