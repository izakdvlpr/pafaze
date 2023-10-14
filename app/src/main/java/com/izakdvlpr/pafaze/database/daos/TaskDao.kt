package com.izakdvlpr.pafaze.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.izakdvlpr.pafaze.models.Task

@Dao
interface TaskDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun create(task: Task)

  @Query("SELECT * FROM tasks")
  fun findMany(): List<Task>

  @Query("SELECT * FROM tasks WHERE id = :id")
  fun findById(id: Int): Task

  @Update
  suspend fun update(task: Task)

  @Query("UPDATE tasks SET done = :done WHERE id =:id")
  suspend fun update(id: Int, done: Boolean)

  @Query("DELETE FROM tasks WHERE id = :id")
  suspend fun delete(id: Int)
}