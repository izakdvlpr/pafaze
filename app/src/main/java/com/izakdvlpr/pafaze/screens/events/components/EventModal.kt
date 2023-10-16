package com.izakdvlpr.pafaze.screens.events.components

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.viewmodels.EventsState
import com.izakdvlpr.pafaze.viewmodels.EventsViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventModal(
  modalBottomSheetState: SheetState,
  eventsState: EventsState,
  eventsViewModel: EventsViewModel
) {
  val startAtDateDialogState = rememberMaterialDialogState()
  val startAtTimeDialogState = rememberMaterialDialogState()

  val endAtDateDialogState = rememberMaterialDialogState()
  val endAtTimeDialogState = rememberMaterialDialogState()

  val titleInputFocusRequester = remember { FocusRequester() }
  val descriptionInputFocusRequester = remember { FocusRequester() }

  val scope = rememberCoroutineScope()

  val modalPadding = 20.dp
  val modalGap = 20.dp

  val buttonPadding = 10.dp

  ModalBottomSheet(
    modifier = Modifier.fillMaxSize(),
    sheetState = modalBottomSheetState,
    onDismissRequest = {
      eventsViewModel.setIsOpenCreateEventModal(!eventsState.isOpenCreateEventModal)
    },
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(modalPadding)
    ) {
      Column(
        modifier = Modifier.padding(bottom = modalGap / 2),
        verticalArrangement = Arrangement.spacedBy(modalGap / 2)
      ) {
        Text(
          text = "Criar evento",
          style = MaterialTheme.typography.titleLarge
        )

        Text(
          text = "Seja notificando quando o evento acontecer.",
          color = MaterialTheme.colorScheme.secondary,
          style = MaterialTheme.typography.bodySmall,
        )
      }

      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(titleInputFocusRequester)
          .onGloballyPositioned {
            if (!eventsState.isTitleTextFieldLoaded) {
              titleInputFocusRequester.requestFocus()

              eventsViewModel.setIsTitleTextFieldLoaded(true)
            }
          },
        value = eventsState.title,
        label = { Text(text = "Título") },
        shape = MaterialTheme.shapes.medium,
        isError = eventsState.titleError != null,
        supportingText = {
          if (eventsState.titleError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = eventsState.titleError,
              color = MaterialTheme.colorScheme.error
            )
          }
        },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
          onNext = {
            if (eventsState.title.isNotBlank()) {
              descriptionInputFocusRequester.requestFocus()
            }
          }
        ),
        onValueChange = { eventsViewModel.setTitle(it) },
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = modalGap / 2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(text = "O evento será o dia todo?")

        Checkbox(
          checked = eventsState.allDay,
          onCheckedChange = { eventsViewModel.setAllDay(it) }
        )
      }

      TextField(
        modifier = Modifier.fillMaxWidth(),
        value = SimpleDateFormat(
          if (eventsState.allDay) "dd/MM/yyyy" else "dd/MM/yyyy HH:mm",
          Locale.getDefault()
        ).format(eventsState.startAt),
        readOnly = true,
        label = { Text(text = "Data de começo") },
        shape = MaterialTheme.shapes.medium,
        isError = eventsState.startAtError != null,
        supportingText = {
          if (eventsState.startAtError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = eventsState.startAtError,
              color = MaterialTheme.colorScheme.error
            )
          }
        },
        interactionSource = remember { MutableInteractionSource() }
          .also { interactionSource ->
            LaunchedEffect(interactionSource) {
              interactionSource.interactions.collect {
                if (it is PressInteraction.Release) {
                  startAtDateDialogState.show()
                }
              }
            }
          },
        onValueChange = { }
      )

      TextField(
        modifier = Modifier.fillMaxWidth(),
        value = SimpleDateFormat(
          if (eventsState.allDay) "dd/MM/yyyy" else "dd/MM/yyyy HH:mm",
          Locale.getDefault()
        ).format(eventsState.endAt),
        readOnly = true,
        label = { Text(text = "Data de finalização") },
        shape = MaterialTheme.shapes.medium,
        isError = eventsState.endAtError != null,
        supportingText = {
          if (eventsState.endAtError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = eventsState.endAtError,
              color = MaterialTheme.colorScheme.error
            )
          }
        },
        interactionSource = remember { MutableInteractionSource() }
          .also { interactionSource ->
            LaunchedEffect(interactionSource) {
              interactionSource.interactions.collect {
                if (it is PressInteraction.Release) {
                  endAtDateDialogState.show()
                }
              }
            }
          },
        onValueChange = { }
      )

      Button(
        modifier = Modifier
          .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = {
          scope
            .launch {
              eventsViewModel.createEvent()
            }
            .invokeOnCompletion {
              if (!modalBottomSheetState.isVisible) {
                eventsViewModel.setIsOpenCreateEventModal(!eventsState.isOpenCreateEventModal)
              }
            }
        }
      ) {
        Text(
          modifier = Modifier.padding(buttonPadding),
          text = "Criar",
          style = MaterialTheme.typography.titleSmall
        )
      }
    }

    EventStartDateDialog(
      eventsState = eventsState,
      eventsViewModel = eventsViewModel,
      startAtDateDialogState = startAtDateDialogState,
      startAtTimeDialogState = startAtTimeDialogState
    )

    EventEndDateDialog(
      eventsState = eventsState,
      eventsViewModel = eventsViewModel,
      endAtDateDialogState = endAtDateDialogState,
      endAtTimeDialogState = endAtTimeDialogState
    )
  }
}