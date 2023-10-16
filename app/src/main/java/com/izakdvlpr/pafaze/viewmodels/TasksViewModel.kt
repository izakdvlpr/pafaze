package com.izakdvlpr.pafaze.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izakdvlpr.pafaze.models.Task
import com.izakdvlpr.pafaze.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
  private val taskRepository: TaskRepository
) : ViewModel() {
  val state by lazy { MutableStateFlow<TasksState>(TasksState()) }

  val event by lazy { MutableSharedFlow<TasksEvent>() }

  fun setIsSearchMode(isSearchMode: Boolean) {
    state.update { it.copy(isSearchMode = isSearchMode) }
  }

  fun setSearch(search: String) {
    state.update { it.copy(search = search) }
  }

  private fun setTasks(tasks: MutableList<Task>) {
    state.update { it.copy(tasks = tasks) }
  }

  fun setIsOpenCreateTaskModal(isOpenCreateTaskModal: Boolean) {
    state.update { it.copy(isOpenCreateTaskModal = isOpenCreateTaskModal) }
  }

  fun setTitle(title: String) {
    state.update { it.copy(title = title) }

    validateTitle(title = title)
  }

  fun updateTitleError(titleError: String?) {
    state.update { it.copy(titleError = titleError) }
  }

  fun setIsTitleTextFieldLoaded(isTitleTextFieldLoaded: Boolean) {
    state.update { it.copy(isTitleTextFieldLoaded = isTitleTextFieldLoaded) }
  }

  private fun validateTitle(title: String): Boolean {
    val titleIsRequired = title.isBlank()

    if (titleIsRequired) {
      updateTitleError("O título é obrigatório.")

      return true
    }

    updateTitleError(null)

    return false
  }

  fun setDescription(description: String) {
    state.update { it.copy(description = description) }
  }

  fun updateDescriptionError(descriptionError: String?) {
    state.update { it.copy(descriptionError = descriptionError) }
  }

  private fun validateDescription(description: String): Boolean {
    val descriptionIsRequired = description.isBlank()

    if (descriptionIsRequired) {
      updateDescriptionError("A descrição é obrigatória.")

      return true
    }

    updateDescriptionError(null)

    return false
  }

  fun getTasks() {
    viewModelScope.launch {
      val tasks = taskRepository.findMany()

      setTasks(tasks = tasks.toMutableList())
    }
  }

  fun createTask() {
    viewModelScope.launch {
      val title = state.value.title
      val description = state.value.description

      val titleValidation = validateTitle(title)

      val validationHasError = listOf(
        titleValidation,
      ).any { it }

      if (validationHasError) {
        return@launch
      }

      taskRepository.create(
        task = Task(
          title = title,
          description = description
        )
      )

      event.emit(TasksEvent.TaskCreated)
    }
  }

  fun doneTask(id: Int, done: Boolean) {
    viewModelScope.launch {
      taskRepository.update(id = id, done = done)

      event.emit(TasksEvent.TaskUpdated)
    }
  }

  fun deleteTask(id: Int) {
    viewModelScope.launch {
      taskRepository.delete(id = id)

      event.emit(TasksEvent.TaskDelete)
    }
  }

  init {
    getTasks()
  }
}

data class TasksState(
  val isSearchMode: Boolean = false,
  val search: String = "",

  val tasks: MutableList<Task> = mutableListOf(),

  val isOpenCreateTaskModal: Boolean = false,

  val title: String = "",
  val titleError: String? = null,
  val isTitleTextFieldLoaded: Boolean = false,

  val description: String = "",
  val descriptionError: String? = null,
)

sealed class TasksEvent {
  object TaskCreated : TasksEvent()
  object TaskUpdated : TasksEvent()
  object TaskDelete : TasksEvent()
}