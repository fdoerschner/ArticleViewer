package de.schwarz.it.data.database

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun stringToStringList(incoming: String): List<String> = incoming.split(",")

    @TypeConverter
    fun stringListToString(incoming: List<String>): String = incoming.joinToString(",")
}