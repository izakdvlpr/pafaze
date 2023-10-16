package com.izakdvlpr.pafaze.screens.events

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
import com.izakdvlpr.pafaze.screens.events.components.EventList
import com.izakdvlpr.pafaze.screens.events.components.EventListEmpty
import com.izakdvlpr.pafaze.screens.events.components.EventModal
import com.izakdvlpr.pafaze.screens.events.components.EventNotFound
import com.izakdvlpr.pafaze.viewmodels.EventsViewModel
import com.izakdvlpr.pafaze.viewmodels.EventsEvent
import com.izakdvlpr.pafaze.viewmodels.EventsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
  navController: NavHostController,
  eventsViewModel: EventsViewModel
) {
  val modalBottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  val eventsState = eventsViewModel.state.collectAsState().value

  BackHandler {
    if (eventsState.isSearchMode) {
      eventsViewModel.setIsSearchMode(false)
      eventsViewModel.setSearch("")
    }
  }

  LaunchedEffect(key1 = modalBottomSheetState.isVisible) {
    if (!modalBottomSheetState.isVisible) {
      eventsViewModel.setIsOpenCreateEventModal(false)
      eventsViewModel.setTitle("")
      eventsViewModel.updateTitleError(null)
      eventsViewModel.setAllDay(false)
      eventsViewModel.setStartAt(EventsState.DEFAULT_START_AT)
      eventsViewModel.updateStartAtError(null)
      eventsViewModel.setEndAt(EventsState.DEFAULT_END_AT)
      eventsViewModel.updateEndAtError(null)
    }
  }

  LaunchedEffect(key1 = Unit) {
    eventsViewModel.event.collect { event ->
      when (event) {
        is EventsEvent.EventCreated -> {
          eventsViewModel.getEvents()

          eventsViewModel.setIsOpenCreateEventModal(false)

          modalBottomSheetState.hide()

          eventsViewModel.setTitle("")
          eventsViewModel.updateTitleError(null)
          eventsViewModel.setAllDay(false)
          eventsViewModel.setStartAt(EventsState.DEFAULT_START_AT)
          eventsViewModel.updateStartAtError(null)
          eventsViewModel.setEndAt(EventsState.DEFAULT_END_AT)
          eventsViewModel.updateEndAtError(null)
        }

        is EventsEvent.EventDelete -> {
          eventsViewModel.getEvents()
        }
      }
    }
  }

  Scaffold(
    topBar = {
      PaFazeTopBarSearch(
        navController = navController,
        search = eventsState.search,
        placeholder = "Procurar evento...",
        isSearchMode = eventsState.isSearchMode,
        onSearchMode = { isSearchMode -> eventsViewModel.setIsSearchMode(isSearchMode) },
        onSearchChangeValue = { search -> eventsViewModel.setSearch(search) },
        onActionBack = { eventsViewModel.setSearch("") }
      )
    },
    floatingActionButton = {
      if (!eventsState.isSearchMode) {
        PaFazeFloatingButton(
          title = "Criar evento",
          onClick = { eventsViewModel.setIsOpenCreateEventModal(!eventsState.isOpenCreateEventModal) }
        )
      }
    },
    floatingActionButtonPosition = FabPosition.End,
    bottomBar = {
      if (!eventsState.isSearchMode) {
        PaFazeBottomBar(navController = navController)
      }
    }
  ) { innerPadding ->
    val screenPadding = 20.dp

    val eventsIsEmpty = eventsState.events.isEmpty()

    val eventsFilterIsEmpty = eventsState.events.none {
      it.title.lowercase() == eventsState.search || it.title.contains(eventsState.search)
    }

    Column(
      modifier = if (eventsIsEmpty || eventsFilterIsEmpty) Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .padding(screenPadding)
      else Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(screenPadding)
    ) {
      if (eventsIsEmpty) {
        EventListEmpty()
      }

      if (eventsState.events.isNotEmpty()) {
        EventList(
          eventsState = eventsState,
          eventsViewModel = eventsViewModel
        )
      }

      if (eventsFilterIsEmpty) {
        EventNotFound()
      }

      if (eventsState.isOpenCreateEventModal) {
        EventModal(
          modalBottomSheetState = modalBottomSheetState,
          eventsState = eventsState,
          eventsViewModel = eventsViewModel
        )
      }
    }
  }
}