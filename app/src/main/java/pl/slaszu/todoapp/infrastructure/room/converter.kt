package pl.slaszu.todoapp.infrastructure.room

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
    @TypeConverter
    fun localDateTimeToString(localeDateTime: LocalDateTime?): String? {
        return localeDateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun stringToLocalDateTime(dateAsString: String?): LocalDateTime? {
        return dateAsString?.let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    }
}