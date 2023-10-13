package com.izakdvlpr.pafaze.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
  val state by lazy { MutableStateFlow<HomeState>(HomeState()) }

  fun resetState() {
    state.value = HomeState()
  }

  fun setIsOpenCreateTaskModal(isOpenCreateTaskModal: Boolean) {
    state.update { it.copy(isOpenCreateTaskModal = isOpenCreateTaskModal) }
  }

  fun setIsSearchMode(isSearchMode: Boolean) {
    state.update { it.copy(isSearchMode = isSearchMode) }
  }

  fun setIsTextFieldLoaded(isTextFieldLoaded: Boolean) {
    state.update { it.copy(isTextFieldLoaded = isTextFieldLoaded) }
  }

  fun setSearch(search: String) {
    state.update { it.copy(search = search) }
  }
}

data class HomeState(
  val isOpenCreateTaskModal: Boolean = false,
  val isSearchMode: Boolean = false,
  val isTextFieldLoaded: Boolean = false,
  val search: String = "",
)