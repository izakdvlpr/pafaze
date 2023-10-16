package com.izakdvlpr.pafaze.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.izakdvlpr.pafaze.components.PaFazeBottomBar
import com.izakdvlpr.pafaze.components.PaFazeFloatingButton
import com.izakdvlpr.pafaze.components.topbars.PaFazeTopBarSearch
import com.izakdvlpr.pafaze.viewmodels.NotesViewModel

@Composable
fun NotesScreen(
  navController: NavHostController,
  notesViewModel: NotesViewModel
) {
  val notesState = notesViewModel.state.collectAsState().value

  BackHandler {
    if (notesState.isSearchMode) {
      notesViewModel.setIsSearchMode(false)
      notesViewModel.setSearch("")
    }
  }

  Scaffold(
    topBar = {
      PaFazeTopBarSearch(
        navController = navController,
        search = notesState.search,
        placeholder = "Procurar nota...",
        isSearchMode = notesState.isSearchMode,
        onSearchMode = { isSearchMode -> notesViewModel.setIsSearchMode(isSearchMode) },
        onSearchChangeValue = { search -> notesViewModel.setSearch(search) },
        onActionBack = { notesViewModel.setSearch("") }
      )
    },
    floatingActionButton = {
      if (!notesState.isSearchMode) {
        PaFazeFloatingButton(
          title = "Criar nota",
          onClick = {  }
        )
      }
    },
    floatingActionButtonPosition = FabPosition.End,
    bottomBar = {
      if (!notesState.isSearchMode) {
        PaFazeBottomBar(navController = navController)
      }
    }
  ) { innerPadding ->
    val screenPadding = 20.dp

    Column(
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(screenPadding)
    ) {

    }
  }
}
