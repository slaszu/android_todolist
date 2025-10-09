package pl.slaszu.todoapp.domain.repeat

import android.content.res.Resources
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.utils.getTypeAndCount
import java.time.LocalDateTime
import java.time.Period

sealed class RepeatType(val period: Period, private val translationKey: Int? = null) {

    class RepeatTypeDay : RepeatType(Period.ofDays(1), R.string.repeat_period_day)
    class RepeatTypeWeek : RepeatType(Period.ofWeeks(1), R.string.repeat_period_week)
    class RepeatTypeMonth : RepeatType(Period.ofMonths(1), R.string.repeat_period_month)
    class RepeatTypeYear : RepeatType(Period.ofYears(1), R.string.repeat_period_year)

    open fun getTranslation(resource: Resources): String {
        if (this.translationKey == null) return "undefined"
        return resource.getString(this.translationKey)
    }

    fun toStringRepresentation(): String {
        return this.period.toString()
    }

    fun calculateNewDate(localDateTime: LocalDateTime): LocalDateTime {
        return localDateTime.plus(this.period)
    }

    companion object {

        fun getAll(): Array<RepeatType> =
            arrayOf(RepeatTypeDay(), RepeatTypeWeek(), RepeatTypeMonth(), RepeatTypeYear())


        fun toObject(string: String): RepeatType {
            return when (val period = Period.parse(string)) {
                RepeatTypeDay().period -> RepeatTypeDay()
                RepeatTypeWeek().period -> RepeatTypeWeek()
                RepeatTypeMonth().period -> RepeatTypeMonth()
                RepeatTypeYear().period -> RepeatTypeYear()
                else -> RepeatTypeOther(period)
            }
        }
    }
}

class RepeatTypeOther(private val otherPeriod: Period) : RepeatType(otherPeriod) {
    override fun getTranslation(resource: Resources): String {
        val periodData = this.period.getTypeAndCount()
        return when(periodData.first) {
            "D" -> resource.getQuantityString(R.plurals.repeat_period_other_d, periodData.second, periodData.second)
            "W" -> resource.getQuantityString(R.plurals.repeat_period_other_w, periodData.second, periodData.second)
            "M" -> resource.getQuantityString(R.plurals.repeat_period_other_m, periodData.second, periodData.second)
            "Y" -> resource.getQuantityString(R.plurals.repeat_period_other_y, periodData.second, periodData.second)
            else -> "undefined"
        }
    }
}


