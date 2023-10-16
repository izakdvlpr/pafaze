package com.izakdvlpr.pafaze.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.izakdvlpr.pafaze.database.converters.DateTypeConverter
import com.izakdvlpr.pafaze.database.daos.TaskDao
import com.izakdvlpr.pafaze.models.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class TaskRoomDatabase : RoomDatabase() {
  abstract fun taskDao(): TaskDao
}