package pl.slaszu.todoapp.infrastructure.room

import android.util.Log
import androidx.room.TypeConverter
import pl.slaszu.todoapp.domain.repeat.RepeatType
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

class RepeatTypeConverter {
    @TypeConverter
    fun repeatTypeToString(repeatType: RepeatType?): String? {
        return repeatType?.toStringRepresentation()
    }

    @TypeConverter
    fun stringToRepeatType(typeAsString: String?): RepeatType? {
        return typeAsString?.let {
            try {
                RepeatType.toObject(typeAsString)
            } catch (e: IllegalArgumentException) {
                Log.d("myapp", "RepeatType for string '$typeAsString' not found !")
                null
            }
        }
    }
}