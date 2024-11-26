package pl.slaszu.todoapp.domain.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun LocalDateTime.toEpochMillis(): Long =
    this.atZone(ZoneId.systemDefault()).toEpochSecond().times(1000)

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this / 1000, 0, ZoneOffset.UTC).atZone(ZoneId.systemDefault()).toLocalDateTime()

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