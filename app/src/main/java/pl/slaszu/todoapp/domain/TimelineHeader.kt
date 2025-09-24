package pl.slaszu.todoapp.domain

import pl.slaszu.todoapp.R

enum class TimelineHeader(val translationResourceKey: Int, val priority: Int) {
    OUT_OF_DATE(R.string.timeline_out_of_date, 1),
    TODAY(R.string.timeline_today, 2),
    THIS_WEEK(R.string.timeline_this_week, 3),
    NEXT_WEEK(R.string.timeline_next_week, 4),
    THIS_MONTH(R.string.timeline_this_month, 5),
    NEXT_MONTH_PLUS(R.string.timeline_next_month_plus, 6),
    NO_DATE(R.string.timeline_no_date, 7),
    FINISHED(R.string.timeline_finished, 8)
}