package com.izakdvlpr.pafaze.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
class Task(
  @PrimaryKey(autoGenerate = true)
  val id: Int = System.currentTimeMillis().hashCode(),
  val title: String,
  val description: String,
  val done: Boolean = false,
)