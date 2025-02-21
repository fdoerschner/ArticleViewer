package de.schwarz.it.data.database

import androidx.room.TypeConverter

/**
 * converters for saving and retrieving lists from and to the database.
 */
@Suppress("UndocumentedPublicFunction")
class ListConverter {
    @TypeConverter
    fun stringToStringList(incoming: String): List<String> = incoming.split(",")

    @TypeConverter
    fun stringListToString(incoming: List<String>): String = incoming.joinToString(",")
}