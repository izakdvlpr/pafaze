package com.izakdvlpr.pafaze.screens.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.viewmodels.HomeState
import com.izakdvlpr.pafaze.viewmodels.HomeViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskModal(
  modalBottomSheetState: SheetState,
  homeState: HomeState,
  homeViewModel: HomeViewModel
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
      homeViewModel.setIsOpenCreateTaskModal(!homeState.isOpenCreateTaskModal)
    },
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(modalPadding),
      verticalArrangement = Arrangement.spacedBy(modalGap / 2)
    ) {
      Column(
        modifier = Modifier.padding(bottom = modalGap / 2),
        verticalArrangement = Arrangement.spacedBy(modalGap / 2)
      ) {
        Text(
          text = "Criar tarefa",
          style = MaterialTheme.typography.titleLarge
        )

        Text(
          text = "Define o que tem \"Pá Faze\" e evite esquecimentos.",
          color = MaterialTheme.colorScheme.secondary,
          style = MaterialTheme.typography.bodySmall,
        )
      }

      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(titleInputFocusRequester)
          .onGloballyPositioned {
            if (!homeState.isTitleTextFieldLoaded) {
              titleInputFocusRequester.requestFocus()

              homeViewModel.setIsTitleTextFieldLoaded(true)
            }
          },
        value = homeState.title,
        label = { Text(text = "Título") },
        shape = MaterialTheme.shapes.medium,
        isError = homeState.titleError != null,
        supportingText = {
          if (homeState.titleError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = homeState.titleError,
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
            if (homeState.title.isNotBlank()) {
              descriptionInputFocusRequester.requestFocus()
            }
          }
        ),
        onValueChange = { homeViewModel.setTitle(it) },
      )

      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(descriptionInputFocusRequester),
        value = homeState.description,
        label = { Text(text = "Descrição") },
        shape = MaterialTheme.shapes.medium,
        isError = homeState.descriptionError != null,
        supportingText = {
          if (homeState.descriptionError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = homeState.descriptionError,
              color = MaterialTheme.colorScheme.error
            )
          }
        },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            if (homeState.description.isNotBlank()) {
              homeViewModel.createTask()
            }
          }
        ),
        onValueChange = { homeViewModel.setDescription(it) }
      )

      TextField(
        modifier = Modifier.fillMaxWidth(),
        value = SimpleDateFormat(
          "dd/MM/yyyy HH:mm",
          Locale.getDefault()
        ).format(homeState.startAt),
        readOnly = true,
        label = { Text(text = "Data de começo") },
        shape = MaterialTheme.shapes.medium,
        isError = homeState.startAtError != null,
        supportingText = {
          if (homeState.startAtError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = homeState.startAtError,
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
          "dd/MM/yyyy HH:mm",
          Locale.getDefault()
        ).format(homeState.endAt),
        readOnly = true,
        label = { Text(text = "Data de finalização") },
        shape = MaterialTheme.shapes.medium,
        isError = homeState.endAtError != null,
        supportingText = {
          if (homeState.endAtError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = homeState.endAtError,
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
              homeViewModel.createTask()
            }
            .invokeOnCompletion {
              if (!modalBottomSheetState.isVisible) {
                homeViewModel.setIsOpenCreateTaskModal(!homeState.isOpenCreateTaskModal)
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

    TaskStartDateDialog(
      homeState = homeState,
      homeViewModel = homeViewModel,
      startAtDateDialogState = startAtDateDialogState,
      startAtTimeDialogState = startAtTimeDialogState
    )

    TaskEndDateDialog(
      homeState = homeState,
      homeViewModel = homeViewModel,
      endAtDateDialogState = endAtDateDialogState,
      endAtTimeDialogState = endAtTimeDialogState
    )
  }
}