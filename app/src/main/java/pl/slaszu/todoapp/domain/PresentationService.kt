package pl.slaszu.todoapp.domain

import java.time.LocalDateTime
import javax.inject.Inject

class PresentationService @Inject constructor(
) {
    fun process(todoList: List<TodoModel>, setting: Setting): List<TodoModel> {
        return if (setting.showDone) {
            todoList
        } else {
            todoList.filter { !it.done }
        }
    }

    fun convertToTimelineMap(todoList: List<TodoModel>): Map<TimelineHeader, List<TodoModel>> {
        return todoList.groupBy {
            getTimelineForTodoModel(it)
        }.toSortedMap(compareBy {
            it.priority
        })
    }

    private fun getTimelineForTodoModel(item: TodoModel): TimelineHeader {
        val now = LocalDateTime.now()
        return when {
            item.startDate == null -> TimelineHeader.NO_DATE
            item.startDate!! < now -> TimelineHeader.OUT_OF_DATE
            item.startDate!! < now.plusDays(7) -> TimelineHeader.THIS_WEEK
            item.startDate!! < now.plusDays(14) -> TimelineHeader.NEXT_WEEK
            item.startDate!! < now.plusMonths(1) -> TimelineHeader.THIS_MONTH
            else -> TimelineHeader.NEXT_MONTH_PLUS
        }
    }
}
