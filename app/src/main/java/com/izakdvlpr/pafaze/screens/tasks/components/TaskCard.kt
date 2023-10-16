package com.izakdvlpr.pafaze.screens.tasks.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.models.Task
import com.izakdvlpr.pafaze.viewmodels.TasksViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun TaskCard(
  task: Task,
  tasksViewModel: TasksViewModel
) {
  val cardPadding = 10.dp
  val cardGap = 10.dp

  val doneAction = SwipeAction(
    icon = rememberVectorPainter(Icons.Outlined.Done),
    background = MaterialTheme.colorScheme.secondary,
    onSwipe = { tasksViewModel.doneTask(id = task.id, done = !task.done) }
  )

  val deleteAction = SwipeAction(
    icon = rememberVectorPainter(Icons.Outlined.Delete),
    background = MaterialTheme.colorScheme.error,
    onSwipe = { tasksViewModel.deleteTask(id = task.id) }
  )

  SwipeableActionsBox(
    startActions = listOf(doneAction),
    endActions = listOf(deleteAction),
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(0.dp),
    ) {
      Row(
        modifier = Modifier
          .fillMaxSize()
          .clickable { tasksViewModel.doneTask(id = task.id, done = !task.done) }
          .padding(cardPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(cardGap / 2),
      ) {
        Checkbox(
          checked = task.done,
          onCheckedChange = { tasksViewModel.doneTask(id = task.id, done = !task.done) }
        )

        if (!task.description.isNullOrBlank()) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = task.title,
              style = MaterialTheme.typography.bodyLarge
            )

            Text(
              text = task.description,
              color = MaterialTheme.colorScheme.secondary,
              style = MaterialTheme.typography.bodySmall
            )
          }
        } else {
          Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge
          )
        }
      }
    }
  }
}