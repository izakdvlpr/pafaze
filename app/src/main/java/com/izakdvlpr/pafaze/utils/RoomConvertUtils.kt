package com.izakdvlpr.pafaze.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.Date

@ProvidedTypeConverter
class RoomConvertUtils {
  @TypeConverter
  fun fromTimestamp(value: Long?): Date? {
    return value?.let { Date(it) }
  }

  @TypeConverter
  fun dateToTimestamp(date: Date?): Long? {
    return date?.time?.toLong()
  }
}