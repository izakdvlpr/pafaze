package com.izakdvlpr.pafaze.screens.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.navigation.NavigationRoutes
import com.izakdvlpr.pafaze.screens.home.components.TaskList
import com.izakdvlpr.pafaze.screens.home.components.TaskListEmpty
import com.izakdvlpr.pafaze.screens.home.components.TaskModal
import com.izakdvlpr.pafaze.screens.home.components.TaskNotFound
import com.izakdvlpr.pafaze.viewmodels.HomeEvent
import com.izakdvlpr.pafaze.viewmodels.HomeState
import com.izakdvlpr.pafaze.viewmodels.HomeViewModel

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

  val searchInputFocusRequester = remember { FocusRequester() }

  val homeState = homeViewModel.state.collectAsState().value

  BackHandler {
    if (homeState.isSearchMode) {
      homeViewModel.setIsSearchMode(false)
      homeViewModel.setIsSearchTextFieldLoaded(false)
      homeViewModel.setSearch("")
    }
  }

  LaunchedEffect(key1 = modalBottomSheetState.isVisible) {
    if (!modalBottomSheetState.isVisible) {
      homeViewModel.setIsOpenCreateTaskModal(false)

      homeViewModel.setIsTitleTextFieldLoaded(false)
      homeViewModel.setTitle("")
      homeViewModel.setDescription("")
      homeViewModel.setStartAt(HomeState.DEFAULT_START_AT)
      homeViewModel.setEndAt(HomeState.DEFAULT_END_AT)
      homeViewModel.updateTitleError(null)
      homeViewModel.updateDescriptionError(null)
      homeViewModel.updateStartAtError(null)
      homeViewModel.updateEndAtError(null)
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
          homeViewModel.setStartAt(HomeState.DEFAULT_START_AT)
          homeViewModel.setEndAt(HomeState.DEFAULT_END_AT)
          homeViewModel.updateTitleError(null)
          homeViewModel.updateDescriptionError(null)
          homeViewModel.updateStartAtError(null)
          homeViewModel.updateEndAtError(null)
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
    },
    floatingActionButton = {
      if (!homeState.isSearchMode) {
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
        TaskListEmpty()
      }

      if (homeState.tasks.isNotEmpty()) {
        TaskList(
          homeState = homeState,
          homeViewModel = homeViewModel
        )
      }

      if (tasksFilterIsEmpty) {
        TaskNotFound()
      }

      if (homeState.isOpenCreateTaskModal) {
        TaskModal(
          modalBottomSheetState = modalBottomSheetState,
          homeState = homeState,
          homeViewModel = homeViewModel
        )
      }
    }
  }
}
