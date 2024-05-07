package io.filmtime.data.database

import androidx.room.TypeConverter

class StringListConverter {

  @TypeConverter
  fun stringToList(stringList: String): List<String> {
    return stringList.split(",")
  }

  @TypeConverter
  fun listToString(list: List<String>): String {
    return list.joinToString(",")
  }
}
