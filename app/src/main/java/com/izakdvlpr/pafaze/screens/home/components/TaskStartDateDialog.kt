package com.izakdvlpr.pafaze.screens.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.izakdvlpr.pafaze.viewmodels.HomeState
import com.izakdvlpr.pafaze.viewmodels.HomeViewModel
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
fun TaskStartDateDialog(
  homeState: HomeState,
  homeViewModel: HomeViewModel,
  startAtDateDialogState: MaterialDialogState,
  startAtTimeDialogState: MaterialDialogState
) {
  MaterialDialog(
    dialogState = startAtDateDialogState,
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
      title = "Selecione a data de começo",
      initialDate = homeState.startAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
      colors = DatePickerDefaults.colors(
        headerBackgroundColor = MaterialTheme.colorScheme.primary,
        headerTextColor = MaterialTheme.colorScheme.onPrimary,
        calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground,
        dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
        dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
        dateInactiveTextColor = MaterialTheme.colorScheme.onBackground,
      ),
      onDateChange = { date ->
        homeViewModel.setStartAt(
          Date.from(
            LocalDateTime
              .of(
                date,
                homeState.startAt.toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
              )
              .atZone(ZoneId.systemDefault()).toInstant()
          )
        )

        startAtTimeDialogState.show()
      }
    )
  }

  MaterialDialog(
    dialogState = startAtTimeDialogState,
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
      title = "Selecione a hora de começo",
      initialTime = homeState.startAt.toInstant().atZone(ZoneId.systemDefault()).toLocalTime(),
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
        homeViewModel.setStartAt(
          Date.from(
            LocalDateTime
              .of(
                homeState.startAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                time
              )
              .atZone(ZoneId.systemDefault()).toInstant()
          )
        )
      }
    )
  }
}