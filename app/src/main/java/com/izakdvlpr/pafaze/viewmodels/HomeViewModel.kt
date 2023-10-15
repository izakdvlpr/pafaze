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
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val taskRepository: TaskRepository
) : ViewModel() {
  val state by lazy { MutableStateFlow<HomeState>(HomeState()) }

  val event by lazy { MutableSharedFlow<HomeEvent>() }

  private fun setTasks(tasks: MutableList<Task>) {
    state.update { it.copy(tasks = tasks) }
  }

  fun setIsSearchMode(isSearchMode: Boolean) {
    state.update { it.copy(isSearchMode = isSearchMode) }
  }

  fun setIsSearchTextFieldLoaded(isSearchTextFieldLoaded: Boolean) {
    state.update { it.copy(isSearchTextFieldLoaded = isSearchTextFieldLoaded) }
  }

  fun setSearch(search: String) {
    state.update { it.copy(search = search) }
  }

  fun setIsOpenCreateTaskModal(isOpenCreateTaskModal: Boolean) {
    state.update { it.copy(isOpenCreateTaskModal = isOpenCreateTaskModal) }
  }

  fun setIsTitleTextFieldLoaded(isTitleTextFieldLoaded: Boolean) {
    state.update { it.copy(isTitleTextFieldLoaded = isTitleTextFieldLoaded) }
  }

  fun setTitle(title: String) {
    state.update { it.copy(title = title) }

    validateTitle(title = title)
  }

  fun updateTitleError(titleError: String?) {
    state.update { it.copy(titleError = titleError) }
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

    validateDescription(description = description)
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

  fun setStartAt(startAt: Date) {
    state.update { it.copy(startAt = startAt) }
  }

  fun updateStartAtError(startAtError: String?) {
    state.update { it.copy(startAtError = startAtError) }
  }

  fun setEndAt(endAt: Date) {
    state.update { it.copy(endAt = endAt) }
  }

  fun updateEndAtError(endAtError: String?) {
    state.update { it.copy(endAtError = endAtError) }
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
      val startAt = state.value.startAt
      val endAt = state.value.endAt

      val titleValidation = validateTitle(title)
      val descriptionValidation = validateDescription(description)

      val validationHasError = listOf(
        titleValidation,
        descriptionValidation
      ).any { it }

      if (validationHasError) {
        return@launch
      }

      taskRepository.create(
        task = Task(
          title = title,
          description = description,
          startAt = startAt,
          endAt = endAt
        )
      )

      event.emit(HomeEvent.TaskCreated)
    }
  }

  fun doneTask(id: Int, done: Boolean) {
    viewModelScope.launch {
      taskRepository.update(id = id, done = done)

      event.emit(HomeEvent.TaskUpdated)
    }
  }

  fun deleteTask(id: Int) {
    viewModelScope.launch {
      taskRepository.delete(id = id)

      event.emit(HomeEvent.TaskDelete)
    }
  }

  init {
    getTasks()
  }
}

data class HomeState(
  val tasks: MutableList<Task> = mutableListOf(),

  val isSearchMode: Boolean = false,
  val isSearchTextFieldLoaded: Boolean = false,
  val search: String = "",

  val isOpenCreateTaskModal: Boolean = false,
  val isTitleTextFieldLoaded: Boolean = false,
  val title: String = "",
  val description: String = "",
  val startAt: Date = DEFAULT_START_AT,
  val endAt: Date = DEFAULT_END_AT,
  val titleError: String? = null,
  val descriptionError: String? = null,
  val startAtError: String? = null,
  val endAtError: String? = null,
) {
  companion object {
    val DEFAULT_START_AT = Date()
    val DEFAULT_END_AT = Date()
  }
}

sealed class HomeEvent {
  object TaskCreated : HomeEvent()
  object TaskUpdated : HomeEvent()
  object TaskDelete : HomeEvent()
}