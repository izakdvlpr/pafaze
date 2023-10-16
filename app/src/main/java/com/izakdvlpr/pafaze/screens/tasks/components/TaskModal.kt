package com.izakdvlpr.pafaze.screens.tasks.components

import android.annotation.SuppressLint
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.izakdvlpr.pafaze.viewmodels.TasksState
import com.izakdvlpr.pafaze.viewmodels.TasksViewModel
import kotlinx.coroutines.launch

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskModal(
  modalBottomSheetState: SheetState,
  tasksState: TasksState,
  tasksViewModel: TasksViewModel
) {
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
      tasksViewModel.setIsOpenCreateTaskModal(!tasksState.isOpenCreateTaskModal)
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
            if (!tasksState.isTitleTextFieldLoaded) {
              titleInputFocusRequester.requestFocus()

              tasksViewModel.setIsTitleTextFieldLoaded(true)
            }
          },
        value = tasksState.title,
        label = { Text(text = "Título") },
        shape = MaterialTheme.shapes.medium,
        isError = tasksState.titleError != null,
        supportingText = {
          if (tasksState.titleError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = tasksState.titleError,
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
            if (tasksState.title.isNotBlank()) {
              descriptionInputFocusRequester.requestFocus()
            }
          }
        ),
        onValueChange = { tasksViewModel.setTitle(it) },
      )

      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .focusRequester(descriptionInputFocusRequester),
        value = tasksState.description,
        label = { Text(text = "Descrição") },
        shape = MaterialTheme.shapes.medium,
        isError = tasksState.descriptionError != null,
        supportingText = {
          if (tasksState.descriptionError != null) {
            Text(
              modifier = Modifier.fillMaxWidth(),
              text = tasksState.descriptionError,
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
            if (tasksState.description.isNotBlank()) {
              tasksViewModel.createTask()
            }
          }
        ),
        onValueChange = { tasksViewModel.setDescription(it) }
      )

      Button(
        modifier = Modifier
          .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = {
          scope
            .launch {
              tasksViewModel.createTask()
            }
            .invokeOnCompletion {
              if (!modalBottomSheetState.isVisible) {
                tasksViewModel.setIsOpenCreateTaskModal(!tasksState.isOpenCreateTaskModal)
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
  }
}