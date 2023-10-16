package com.izakdvlpr.pafaze.viewmodels

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izakdvlpr.pafaze.broadcasters.EventReceiver
import com.izakdvlpr.pafaze.models.Event
import com.izakdvlpr.pafaze.models.Task
import com.izakdvlpr.pafaze.repositories.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class EventsViewModel @Inject constructor (
  private val eventRepository: EventRepository,
  application: Application
) : AndroidViewModel(application) {
  @SuppressLint("StaticFieldLeak")
  val context = application as Context

  val state by lazy { MutableStateFlow<EventsState>(EventsState()) }

  val event by lazy { MutableSharedFlow<EventsEvent>() }

  fun setIsSearchMode(isSearchMode: Boolean) {
    state.update { it.copy(isSearchMode = isSearchMode) }
  }

  fun setSearch(search: String) {
    state.update { it.copy(search = search) }
  }

  private fun setEvents(events: MutableList<Event>) {
    state.update { it.copy(events = events) }
  }

  fun setIsOpenCreateEventModal(isOpenCreateEventModal: Boolean) {
    state.update { it.copy(isOpenCreateEventModal = isOpenCreateEventModal) }
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

  fun setAllDay(allDay: Boolean) {
    state.update { it.copy(allDay = allDay) }
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

  fun getEvents() {
    viewModelScope.launch {
      val events = eventRepository.findMany()

      setEvents(events = events.toMutableList())
    }
  }

  @SuppressLint("ServiceCast")
  fun createEvent() {
    viewModelScope.launch {
      val title = state.value.title
      val allDay = state.value.allDay
      val startAt = state.value.startAt
      val endAt = state.value.endAt

      val titleValidation = validateTitle(title)

      val validationHasError = listOf(
        titleValidation,
      ).any { it }

      if (validationHasError) {
        return@launch
      }

      eventRepository.create(
        event = Event(
          title = title,
          allDay = allDay,
          startAt = startAt,
          endAt = endAt,
        )
      )

      event.emit(EventsEvent.EventCreated)
    }
  }

  fun deleteEvent(id: Int) {
    viewModelScope.launch {
      eventRepository.delete(id = id)

      event.emit(EventsEvent.EventDelete)
    }
  }

  init {
    getEvents()
  }
}

data class EventsState(
  val isSearchMode: Boolean = false,
  val search: String = "",

  val events: MutableList<Event> = mutableListOf(),

  val isOpenCreateEventModal: Boolean = false,

  val title: String = "",
  val titleError: String? = null,
  val isTitleTextFieldLoaded: Boolean = false,

  val allDay: Boolean = false,

  val startAt: Date = DEFAULT_START_AT,
  val startAtError: String? = null,

  val endAt: Date = DEFAULT_END_AT,
  val endAtError: String? = null,
) {
  companion object {
    val DEFAULT_START_AT = Date()
    val DEFAULT_END_AT = Date()
  }
}

sealed class EventsEvent {
  object EventCreated : EventsEvent()
  object EventDelete : EventsEvent()
}