package com.izakdvlpr.pafaze.modules

import android.content.Context
import androidx.room.Room
import com.izakdvlpr.pafaze.database.EventRoomDatabase
import com.izakdvlpr.pafaze.database.TaskRoomDatabase
import com.izakdvlpr.pafaze.database.daos.EventDao
import com.izakdvlpr.pafaze.database.daos.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
  @Provides
  @Singleton
  fun provideTaskRoomDatabase(@ApplicationContext applicationContext: Context): TaskRoomDatabase {
    return Room.databaseBuilder(applicationContext, TaskRoomDatabase::class.java, "pafaze_tasks")
      .allowMainThreadQueries()
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  @Singleton
  fun provideEventRoomDatabase(@ApplicationContext applicationContext: Context): EventRoomDatabase {
    return Room.databaseBuilder(applicationContext, EventRoomDatabase::class.java, "pafaze_events")
      .allowMainThreadQueries()
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  fun provideTaskDao(taskRoomDatabase: TaskRoomDatabase): TaskDao {
    return taskRoomDatabase.taskDao()
  }

  @Provides
  fun provideEventDao(eventRoomDatabase: EventRoomDatabase): EventDao {
    return eventRoomDatabase.taskDao()
  }
}