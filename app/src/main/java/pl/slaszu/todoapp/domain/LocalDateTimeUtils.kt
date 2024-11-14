package pl.slaszu.todoapp.domain

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun LocalDateTime.toEpochMillis(): Long =
    this.toEpochSecond(ZoneOffset.UTC).times(1000)

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this / 1000, 0, ZoneOffset.UTC)

fun LocalDateTime.print(): String =
    "${this.printDate()} ${this.printTime()}"

fun LocalDateTime.printDate(): String =
    this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

fun LocalDateTime.printTime(): String? {
    if (this.isTimeSet()) {
        return this.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
    return null
}

fun LocalDateTime.isTimeSet(): Boolean {
    return this.format(DateTimeFormatter.ofPattern("s")) != "0"
}

fun LocalDateTime.clearTime(): LocalDateTime {
    return this.withHour(0).withMinute(0).withSecond(0).withNano(0)
}

fun LocalDateTime.setTime(hour: Int, minute: Int): LocalDateTime {
    return this.clearTime().withHour(hour).withMinute(minute).withSecond(1);
}

@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerState.toMillis(): Int =
    (this.hour.times(60 * 60) + this.minute.times(60)).times(1000)