package com.izakdvlpr.pafaze.screens.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.viewmodels.EventsState
import com.izakdvlpr.pafaze.viewmodels.EventsViewModel

@Composable
fun EventList(
  eventsState: EventsState,
  eventsViewModel: EventsViewModel
) {
  val containerGap = 20.dp

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(containerGap / 2)
  ) {
    eventsState.events
      .filter {
        it.title.lowercase() == eventsState.search || it.title.contains(eventsState.search)
      }
      .forEach { event ->
        EventCard(
          event = event,
          eventsViewModel = eventsViewModel
        )
      }
  }
}