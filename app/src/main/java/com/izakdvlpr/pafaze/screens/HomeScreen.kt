package com.izakdvlpr.pafaze.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.models.Task
import com.izakdvlpr.pafaze.navigation.NavigationRoutes
import com.izakdvlpr.pafaze.viewmodels.HomeEvent
import com.izakdvlpr.pafaze.viewmodels.HomeState
import com.izakdvlpr.pafaze.viewmodels.HomeViewModel
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
  navController: NavHostController,
  homeViewModel: HomeViewModel
) {
  val modalBottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  val homeState = homeViewModel.state.collectAsState().value

  BackHandler {
    if (homeState.isSearchMode) {
      homeViewModel.setIsSearchMode(false)
      homeViewModel.setIsSearchTextFieldLoaded(false)
      homeViewModel.setSearch("")
    }
  }

  LaunchedEffect(key1 = Unit) {
    homeViewModel.event.collect { event ->
      when (event) {
        is HomeEvent.TaskCreated -> {
          homeViewModel.getTasks()

          homeViewModel.setIsOpenCreateTaskModal(false)

          modalBottomSheetState.hide()

          homeViewModel.setIsTitleTextFieldLoaded(false)
          homeViewModel.setTitle("")
          homeViewModel.setDescription("")
        }

        is HomeEvent.TaskUpdated -> {
          homeViewModel.getTasks()
        }

        is HomeEvent.TaskDelete -> {
          homeViewModel.getTasks()
        }
      }
    }
  }

  Scaffold(
    topBar = {
      TopBar(
        navController = navController,
        homeState = homeState,
        homeViewModel = homeViewModel
      )
    },
    floatingActionButton = {
      if (!homeState.isSearchMode) {
        CreateTaskButton(
          homeState = homeState,
          homeViewModel = homeViewModel
        )
      }
    },
    floatingActionButtonPosition = FabPosition.Center,
  ) { innerPadding ->
    val screenPadding = 20.dp

    val tasksIsEmpty = homeState.tasks.isEmpty()

    val tasksFilterIsEmpty = homeState.tasks.none {
      it.title.lowercase() == homeState.search || it.title.contains(homeState.search)
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
        TasksEmpty()
      }

      if (homeState.tasks.isNotEmpty()) {
        TasksList(
          homeState = homeState,
          homeViewModel = homeViewModel
        )
      }

      if (tasksFilterIsEmpty) {
        TasksNotFound()
      }

      if (homeState.isOpenCreateTaskModal) {
        CreateTaskModal(
          modalBottomSheetState = modalBottomSheetState,
          homeState = homeState,
          homeViewModel = homeViewModel
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
  navController: NavHostController,
  homeState: HomeState,
  homeViewModel: HomeViewModel
) {
  val searchInputFocusRequester = remember { FocusRequester() }

  TopAppBar(
    navigationIcon = {
      if (homeState.isSearchMode) {
        IconButton(
          onClick = {
            homeViewModel.setIsSearchMode(false)
            homeViewModel.setIsSearchTextFieldLoaded(false)
            homeViewModel.setSearch("")
          }
        ) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back"
          )
        }
      }
    },
    title = {
      if (homeState.isSearchMode) {
        BasicTextField(
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(searchInputFocusRequester)
            .onGloballyPositioned {
              if (!homeState.isSearchTextFieldLoaded) {
                searchInputFocusRequester.requestFocus()

                homeViewModel.setIsSearchTextFieldLoaded(true)
              }
            },
          value = homeState.search,
          maxLines = 1,
          singleLine = true,
          cursorBrush = SolidColor(Color.White),
          textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
          decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxWidth()) {
              if (homeState.search.isEmpty()) Text(
                text = "Procurar tarefa...",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium
              )
            }

            innerTextField()
          },
          onValueChange = { homeViewModel.setSearch(it) }
        )
      } else {
        Text(text = "PaFaze")
      }
    },
    actions = {
      if (homeState.isSearchMode) {
        Box(modifier = Modifier.width(40.dp))
      } else {
        IconButton(
          onClick = {
            homeViewModel.setIsSearchMode(!homeState.isSearchMode)
          }
        ) {
          Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Search"
          )
        }

        IconButton(onClick = { navController.navigate(NavigationRoutes.settings) }) {
          Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = "Settings"
          )
        }
      }

    }
  )
}

@Composable
private fun CreateTaskButton(
  homeState: HomeState,
  homeViewModel: HomeViewModel
) {
  FloatingActionButton(
    modifier = Modifier.size(70.dp),
    shape = CircleShape,
    onClick = { homeViewModel.setIsOpenCreateTaskModal(!homeState.isOpenCreateTaskModal) }
  ) {
    Icon(
      imageVector = Icons.Outlined.AddBox,
      contentDescription = "Add"
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTaskModal(
  modalBottomSheetState: SheetState,
  homeState: HomeState,
  homeViewModel: HomeViewModel
) {
  val titleInputFocusRequester = remember { FocusRequester() }
  val descriptionInputFocusRequester = remember { FocusRequester() }

  val scope = rememberCoroutineScope()

  val modalPadding = 20.dp
  val modalGap = 20.dp

  val buttonPadding = 10.dp

  ModalBottomSheet(
    modifier = Modifier
      .fillMaxSize(),
    sheetState = modalBottomSheetState,
    onDismissRequest = {
      homeViewModel.setIsOpenCreateTaskModal(!homeState.isOpenCreateTaskModal)
    },
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(modalPadding),
      verticalArrangement = Arrangement.spacedBy(modalGap)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(modalGap / 2)) {
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

      Button(
        modifier = Modifier.fillMaxWidth(),
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
  }
}

@Composable
private fun TasksEmpty() {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      modifier = Modifier.size(80.dp),
      imageVector = Icons.Outlined.EditCalendar,
      contentDescription = "EditNote"
    )

    Text(
      text = "Nenhuma\ntarefa criada",
      style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
    )

    Text(
      modifier = Modifier.padding(top = 10.dp),
      text = "Clique no botão abaixo para\ncriar sua primeira tarefa!",
      color = MaterialTheme.colorScheme.primary,
      style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
    )
  }
}

@Composable
private fun TasksNotFound() {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      modifier = Modifier.size(80.dp),
      imageVector = Icons.Outlined.SentimentDissatisfied,
      contentDescription = "SentimentDissatisfied"
    )

    Text(
      text = "Nenhuma\ntarefa encontrada",
      style = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center)
    )

    Text(
      modifier = Modifier.padding(top = 10.dp),
      text = "Certifique-se de ter colocado\nos argumentos corretamente.",
      color = MaterialTheme.colorScheme.primary,
      style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
    )
  }
}

@Composable
private fun TasksList(
  homeState: HomeState,
  homeViewModel: HomeViewModel
) {
  val containerGap = 20.dp

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(containerGap / 2)
  ) {
//    homeState.tasks.sortBy { it.done }
    homeState.tasks
      .filter {
        it.title.lowercase() == homeState.search || it.title.contains(homeState.search)
      }
      .forEach { task ->
        TaskCard(
          task = task,
          homeViewModel = homeViewModel
        )
      }
  }
}

@Composable
private fun TaskCard(
  task: Task,
  homeViewModel: HomeViewModel
) {
  val cardPadding = 10.dp
  val cardGap = 10.dp

  val doneAction = SwipeAction(
    icon = rememberVectorPainter(Icons.Outlined.Done),
    background = MaterialTheme.colorScheme.secondary,
    onSwipe = { homeViewModel.doneTask(id = task.id, done = !task.done) }
  )

  val deleteAction = SwipeAction(
    icon = rememberVectorPainter(Icons.Outlined.Delete),
    background = MaterialTheme.colorScheme.error,
    onSwipe = { homeViewModel.deleteTask(id = task.id) }
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
          .clickable { homeViewModel.doneTask(id = task.id, done = !task.done) }
          .padding(cardPadding),
        horizontalArrangement = Arrangement.spacedBy(cardGap / 2)
      ) {
        Checkbox(
          checked = task.done,
          onCheckedChange = { homeViewModel.doneTask(id = task.id, done = !task.done) }
        )

        Column {
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
      }
    }
  }
}