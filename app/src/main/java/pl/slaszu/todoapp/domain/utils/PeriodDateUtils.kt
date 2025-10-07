package pl.slaszu.todoapp.domain.utils

import java.time.Period

fun Period.getTypeAndCount(): Pair<String, Int> {
    return when {
        this.years > 0 -> Pair("Y", this.years)
        this.months > 0 -> Pair("M", this.months)
        this.days % 7 == 0 -> Pair("W", this.days / 7)
        else -> Pair("D", this.days)
    }
}

fun createPeriodFromTypeAndCount(type: String, count: Int): Period {
    return when (type) {
        "Y" -> Period.ofYears(count)
        "M" -> Period.ofMonths(count)
        "W" -> Period.ofWeeks(count)
        else -> Period.ofDays(count)

    }
}