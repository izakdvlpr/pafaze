package com.izakdvlpr.pafaze.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.izakdvlpr.pafaze.models.Event

@Dao
interface EventDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun create(event: Event)

  @Query("SELECT * FROM events")
  fun findMany(): List<Event>

  @Query("SELECT * FROM events WHERE id = :id")
  fun findById(id: Int): Event

  @Update
  suspend fun update(event: Event)

  @Query("DELETE FROM events WHERE id = :id")
  suspend fun delete(id: Int)
}