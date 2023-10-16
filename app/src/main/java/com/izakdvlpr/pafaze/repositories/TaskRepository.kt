package com.izakdvlpr.pafaze.repositories

import com.izakdvlpr.pafaze.database.daos.TaskDao
import com.izakdvlpr.pafaze.models.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
  suspend fun create(task: Task) = taskDao.create(task)

  suspend fun findMany() = taskDao.findMany()

  suspend fun findById(id: Int) = taskDao.findById(id)

  suspend fun update(task: Task) = taskDao.update(task)

  suspend fun update(id: Int, done: Boolean) = taskDao.update(id, done)

  suspend fun delete(id: Int) = taskDao.delete(id)
}