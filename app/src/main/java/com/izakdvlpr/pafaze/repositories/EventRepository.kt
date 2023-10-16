package com.izakdvlpr.pafaze.repositories

import com.izakdvlpr.pafaze.database.daos.EventDao
import com.izakdvlpr.pafaze.models.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val eventDao: EventDao) {
  suspend fun create(event: Event) = eventDao.create(event)

  suspend fun findMany() = eventDao.findMany()

  suspend fun findById(id: Int) = eventDao.findById(id)

  suspend fun update(event: Event) = eventDao.update(event)

  suspend fun delete(id: Int) = eventDao.delete(id)
}