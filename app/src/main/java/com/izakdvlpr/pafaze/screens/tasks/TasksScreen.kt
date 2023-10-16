package com.izakdvlpr.pafaze.screens.tasks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.components.PaFazeBottomBar
import com.izakdvlpr.pafaze.components.PaFazeFloatingButton
import com.izakdvlpr.pafaze.components.topbars.PaFazeTopBarSearch
import com.izakdvlpr.pafaze.screens.tasks.components.TaskList
import com.izakdvlpr.pafaze.screens.tasks.components.TaskListEmpty
import com.izakdvlpr.pafaze.screens.tasks.components.TaskModal
import com.izakdvlpr.pafaze.screens.tasks.components.TaskNotFound
import com.izakdvlpr.pafaze.viewmodels.TasksEvent
import com.izakdvlpr.pafaze.viewmodels.TasksState
import com.izakdvlpr.pafaze.viewmodels.TasksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
  navController: NavHostController,
  tasksViewModel: TasksViewModel
) {
  val modalBottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  val tasksState = tasksViewModel.state.collectAsState().value

  BackHandler {
    if (tasksState.isSearchMode) {
      tasksViewModel.setIsSearchMode(false)
      tasksViewModel.setSearch("")
    }
  }

  LaunchedEffect(key1 = modalBottomSheetState.isVisible) {
    if (!modalBottomSheetState.isVisible) {
      tasksViewModel.setIsOpenCreateTaskModal(false)
      tasksViewModel.setTitle("")
      tasksViewModel.setDescription("")
      tasksViewModel.updateTitleError(null)
      tasksViewModel.updateDescriptionError(null)
    }
  }

  LaunchedEffect(key1 = Unit) {
    tasksViewModel.event.collect { event ->
      when (event) {
        is TasksEvent.TaskCreated -> {
          tasksViewModel.getTasks()

          tasksViewModel.setIsOpenCreateTaskModal(false)

          modalBottomSheetState.hide()

          tasksViewModel.setTitle("")
          tasksViewModel.setDescription("")
          tasksViewModel.updateTitleError(null)
          tasksViewModel.updateDescriptionError(null)
        }

        is TasksEvent.TaskUpdated -> {
          tasksViewModel.getTasks()
        }

        is TasksEvent.TaskDelete -> {
          tasksViewModel.getTasks()
        }
      }
    }
  }

  Scaffold(
    topBar = {
      PaFazeTopBarSearch(
        navController = navController,
        search = tasksState.search,
        placeholder = "Procurar tarefa...",
        isSearchMode = tasksState.isSearchMode,
        onSearchMode = { isSearchMode -> tasksViewModel.setIsSearchMode(isSearchMode) },
        onSearchChangeValue = { search -> tasksViewModel.setSearch(search) },
        onActionBack = { tasksViewModel.setSearch("") }
      )
    },
    floatingActionButton = {
      if (!tasksState.isSearchMode) {
        PaFazeFloatingButton(
          title = "Criar tarefa",
          onClick = { tasksViewModel.setIsOpenCreateTaskModal(!tasksState.isOpenCreateTaskModal) }
        )
      }
    },
    floatingActionButtonPosition = FabPosition.End,
    bottomBar = {
      if (!tasksState.isSearchMode) {
        PaFazeBottomBar(navController = navController)
      }
    }
  ) { innerPadding ->
    val screenPadding = 20.dp

    val tasksIsEmpty = tasksState.tasks.isEmpty()

    val tasksFilterIsEmpty = tasksState.tasks.none {
      it.title.lowercase() == tasksState.search || it.title.contains(tasksState.search)
    }

    Column(
      modifier = if (tasksIsEmpty || tasksFilterIsEmpty) Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .padding(screenPadding)
      else Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(screenPadding)
    ) {
      if (tasksIsEmpty) {
        TaskListEmpty()
      }

      if (tasksState.tasks.isNotEmpty()) {
        TaskList(
          tasksState = tasksState,
          tasksViewModel = tasksViewModel
        )
      }

      if (tasksFilterIsEmpty) {
        TaskNotFound()
      }

      if (tasksState.isOpenCreateTaskModal) {
        TaskModal(
          modalBottomSheetState = modalBottomSheetState,
          tasksState = tasksState,
          tasksViewModel = tasksViewModel
        )
      }
    }
  }
}
