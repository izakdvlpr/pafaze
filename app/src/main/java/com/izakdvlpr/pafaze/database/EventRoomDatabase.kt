package com.izakdvlpr.pafaze.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.izakdvlpr.pafaze.database.converters.DateTypeConverter
import com.izakdvlpr.pafaze.database.daos.EventDao
import com.izakdvlpr.pafaze.models.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class EventRoomDatabase : RoomDatabase() {
  abstract fun taskDao(): EventDao
}