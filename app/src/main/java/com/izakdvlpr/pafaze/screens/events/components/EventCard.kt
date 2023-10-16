package com.izakdvlpr.pafaze.screens.events.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.models.Event
import com.izakdvlpr.pafaze.viewmodels.EventsViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventCard(
  event: Event,
  eventsViewModel: EventsViewModel
) {
  val cardPadding = 10.dp

  val deleteAction = SwipeAction(
    icon = rememberVectorPainter(Icons.Outlined.Delete),
    background = MaterialTheme.colorScheme.error,
    onSwipe = { eventsViewModel.deleteEvent(id = event.id) }
  )

  SwipeableActionsBox(
    endActions = listOf(deleteAction),
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(0.dp),
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .clickable { }
          .padding(cardPadding)
      ) {
        Text(
          text = event.title,
          style = MaterialTheme.typography.bodyLarge
        )

        Text(
          text = SimpleDateFormat(
            if (event.allDay) "dd/MM/yyyy" else "dd/MM/yyyy HH:mm",
            Locale.getDefault()
          ).format(event.endAt),
          color = MaterialTheme.colorScheme.tertiary,
          style = MaterialTheme.typography.bodySmall
        )
      }
    }
  }
}