package com.izakdvlpr.pafaze.models

import java.util.UUID

class Task(
  val id: UUID,
  val title: String,
  val description: String,
  val done: Boolean,
)