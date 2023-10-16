package com.izakdvlpr.pafaze.screens.events.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.izakdvlpr.pafaze.viewmodels.EventsState
import com.izakdvlpr.pafaze.viewmodels.EventsViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Composable
fun EventEndDateDialog(
  eventsState: EventsState,
  eventsViewModel: EventsViewModel,
  endAtDateDialogState: MaterialDialogState,
  endAtTimeDialogState: MaterialDialogState
) {
  MaterialDialog(
    dialogState = endAtDateDialogState,
    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
    buttons = {
      positiveButton(
        text = "Próximo",
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
      )
      negativeButton(
        text = "Canncelar",
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
      )
    }
  ) {
    datepicker(
      title = "Selecione a data de finalização",
      initialDate = eventsState.endAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
      colors = DatePickerDefaults.colors(
        headerBackgroundColor = MaterialTheme.colorScheme.primary,
        headerTextColor = MaterialTheme.colorScheme.onPrimary,
        calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground,
        dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
        dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
        dateInactiveTextColor = MaterialTheme.colorScheme.onBackground,
      ),
      onDateChange = { date ->
        eventsViewModel.setEndAt(
          Date.from(
            LocalDateTime
              .of(
                date,
                eventsState.endAt.toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
              )
              .atZone(ZoneId.systemDefault()).toInstant()
          )
        )

        if (!eventsState.allDay) {
          endAtTimeDialogState.show()
        }
      }
    )
  }

  MaterialDialog(
    dialogState = endAtTimeDialogState,
    backgroundColor = MaterialTheme.colorScheme.primaryContainer,
    buttons = {
      positiveButton(
        text = "Próximo",
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
      )
      negativeButton(
        text = "Canncelar",
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
      )
    }
  ) {
    timepicker(
      title = "Selecione a hora de finalização",
      initialTime = eventsState.endAt.toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
      is24HourClock = true,
      colors = TimePickerDefaults.colors(
        activeBackgroundColor = MaterialTheme.colorScheme.primary.copy(0.3f),
        inactiveBackgroundColor = MaterialTheme.colorScheme.onBackground.copy(0.3f),
        activeTextColor = MaterialTheme.colorScheme.onPrimary,
        inactiveTextColor = MaterialTheme.colorScheme.onBackground,
        selectorColor = MaterialTheme.colorScheme.primary,
        selectorTextColor = MaterialTheme.colorScheme.onPrimary,
        headerTextColor = MaterialTheme.colorScheme.onBackground,
        borderColor = MaterialTheme.colorScheme.onBackground
      ),
      onTimeChange = { time ->
        eventsViewModel.setEndAt(
          Date.from(
            LocalDateTime
              .of(
                eventsState.endAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                time
              )
              .atZone(ZoneId.systemDefault()).toInstant()
          )
        )
      }
    )
  }
}