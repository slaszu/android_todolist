package pl.slaszu.todoapp.domain.repeat

import pl.slaszu.todoapp.R
import java.time.LocalDateTime
import java.time.Period

sealed class RepeatType(val period: Period, val translationKey: Int? = null) {

    class RepeatTypeDay : RepeatType(Period.ofDays(1), R.string.repeat_period_day)
    class RepeatTypeWeek : RepeatType(Period.ofWeeks(1), R.string.repeat_period_week)
    class RepeatTypeMonth : RepeatType(Period.ofMonths(1), R.string.repeat_period_month)
    class RepeatTypeOther(private val otherPeriod: Period) : RepeatType(otherPeriod)

    fun toStringRepresentation(): String {
        return this.period.toString()
    }

    fun calculateNewDate(localDateTime: LocalDateTime): LocalDateTime {
        return localDateTime.plus(this.period)
    }

    companion object {

        fun getAll(): Array<RepeatType> =
            arrayOf(RepeatTypeDay(), RepeatTypeWeek(), RepeatTypeMonth())


        fun toObject(string: String): RepeatType {
            return when (val period = Period.parse(string)) {
                RepeatTypeDay().period -> RepeatTypeDay()
                RepeatTypeWeek().period -> RepeatTypeWeek()
                RepeatTypeMonth().period -> RepeatTypeMonth()
                else -> RepeatTypeOther(period)
            }
        }
    }
}


