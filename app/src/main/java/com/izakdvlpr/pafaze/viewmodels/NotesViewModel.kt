package com.izakdvlpr.pafaze.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor () : ViewModel() {
  val state by lazy { MutableStateFlow<NotesState>(NotesState()) }

  fun setIsSearchMode(isSearchMode: Boolean) {
    state.update { it.copy(isSearchMode = isSearchMode) }
  }

  fun setSearch(search: String) {
    state.update { it.copy(search = search) }
  }
}

data class NotesState(
  val isSearchMode: Boolean = false,
  val search: String = "",
)