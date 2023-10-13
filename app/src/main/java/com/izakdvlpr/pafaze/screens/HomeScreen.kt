package com.izakdvlpr.pafaze.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.navigation.NavigationRoutes
import com.izakdvlpr.pafaze.viewmodels.HomeState
import com.izakdvlpr.pafaze.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
  navController: NavHostController,
  homeViewModel: HomeViewModel
) {
  val homeState = homeViewModel.state.collectAsState().value

  BackHandler {
    if (homeState.isSearchMode) {
      homeViewModel.resetState()
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

    Column(
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .padding(screenPadding)
    ) {
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

      if (homeState.isOpenCreateTaskModal) {
        CreateTaskModal(
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
  val inputFocusRequester = remember { FocusRequester() }

  if (homeState.isSearchMode) {
    TopAppBar(
      navigationIcon = {
        IconButton(onClick = { homeViewModel.resetState() }) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back"
          )
        }
      },
      title = {
        BasicTextField(
          value = homeState.search,
          maxLines = 1,
          singleLine = true,
          textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(inputFocusRequester)
            .onGloballyPositioned {
              if (!homeState.isTextFieldLoaded) {
                inputFocusRequester.requestFocus()

                homeViewModel.setIsTextFieldLoaded(true)
              }
            },
          onValueChange = { homeViewModel.setSearch(it) }
        )
      },
      actions = { Box(modifier = Modifier.width(40.dp)) }
    )
  } else {
    TopAppBar(
      title = { Text(text = "PaFaze") },
      actions = {
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
      },
      colors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
      ),
    )
  }
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
  homeState: HomeState,
  homeViewModel: HomeViewModel
) {
  val modalBottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  val scope = rememberCoroutineScope()

  val (title, setTitle) = remember { mutableStateOf("") }
  val (description, setDescription) = remember { mutableStateOf("") }

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
        modifier = Modifier.fillMaxWidth(),
        value = title,
        label = { Text(text = "Título") },
        onValueChange = { setTitle(it) }
      )

      TextField(
        modifier = Modifier.fillMaxWidth(),
        value = description,
        label = { Text(text = "Descrição") },
        onValueChange = { setDescription(it) }
      )

      Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          scope
            .launch {
              modalBottomSheetState.hide()
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