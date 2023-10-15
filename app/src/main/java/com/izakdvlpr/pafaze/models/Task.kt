package com.izakdvlpr.pafaze.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "tasks")
class Task(
  @PrimaryKey(autoGenerate = true)
  val id: Int = System.currentTimeMillis().hashCode(),
  val title: String,
  val description: String,
  val done: Boolean = false,
  val notify: Boolean = false,
  val startAt: Date? = null,
  val endAt: Date? = null,
)