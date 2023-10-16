package com.izakdvlpr.pafaze.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
class Event(
  @PrimaryKey(autoGenerate = true)
  val id: Int = System.currentTimeMillis().hashCode(),
  val title: String,
  val allDay: Boolean = false,
  val startAt: Date,
  val endAt: Date,
)