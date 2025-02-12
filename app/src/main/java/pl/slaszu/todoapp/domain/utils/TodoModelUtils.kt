package pl.slaszu.todoapp.domain.utils

import pl.slaszu.todoapp.domain.TodoModel

fun TodoModel.printStartDate(noDate: String, noTime: String): String {
    return when {
        this.startDate == null -> noDate
        !this.startDate!!.isTimeSet() -> this.startDate!!.printDate() + " ($noTime)"
        else -> this.startDate!!.printDate() + " " + this.startDate!!.printTime()
    }
}