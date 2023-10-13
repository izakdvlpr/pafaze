package com.izakdvlpr.pafaze.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
  val state by lazy { MutableStateFlow<SettingsState>(SettingsState()) }

  fun setCurrentDialog(currentDialog: DialogTypes?) {
    state.update { it.copy(currentDialog = currentDialog) }
  }

  fun setIsOpenDialog(isOpenDialog: Boolean) {
    state.update { it.copy(isOpenDialog = isOpenDialog) }
  }
}

enum class DialogTypes {
  THEME_DIALOG,
  LANGUAGE_DIALOG
}

data class SettingsState(
  val currentDialog: DialogTypes? = null,
  val isOpenDialog: Boolean = false,
)