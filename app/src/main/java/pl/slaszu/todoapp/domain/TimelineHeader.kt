package pl.slaszu.todoapp.domain

import pl.slaszu.todoapp.R

enum class TimelineHeader(val translationResourceKey: Int, val priority: Int) {
    OUT_OF_DATE(R.string.timeline_out_of_date, 1),
    THIS_WEEK(R.string.timeline_this_week, 2),
    NEXT_WEEK(R.string.timeline_next_week, 3),
    THIS_MONTH(R.string.timeline_this_month, 4),
    NEXT_MONTH_PLUS(R.string.timeline_next_month_plus, 5),
    NO_DATE(R.string.timeline_no_date, 6)
}