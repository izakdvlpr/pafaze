package com.izakdvlpr.pafaze.modules

import android.content.Context
import androidx.room.Room
import com.izakdvlpr.pafaze.database.TaskRoomDatabase
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
    return Room.databaseBuilder(applicationContext, TaskRoomDatabase::class.java, "tasks")
      .allowMainThreadQueries()
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  fun provideTaskDao(taskRoomDatabase: TaskRoomDatabase): TaskDao {
    return taskRoomDatabase.taskDao()
  }
}