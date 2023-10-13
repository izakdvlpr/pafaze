package com.izakdvlpr.pafaze.viewmodels

import androidx.lifecycle.ViewModel
import com.izakdvlpr.pafaze.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor() : ViewModel() {
}

data class TaskState(
  val tasks: MutableList<Task> = mutableListOf(),
)