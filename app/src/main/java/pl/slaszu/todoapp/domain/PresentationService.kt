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
        }
    }

    private fun getTimelineForTodoModel(item: TodoModel): TimelineHeader {
        val now = LocalDateTime.now()
        return when {
            item.startDate == null -> TimelineHeader.OTHER
            item.startDate!! > now.minusDays(7) -> TimelineHeader.THIS_WEEK
            item.startDate!! > now.minusDays(14) -> TimelineHeader.NEXT_WEEK
            item.startDate!! > now.minusMonths(1) -> TimelineHeader.THIS_MONTH
            else -> TimelineHeader.OTHER
        }
    }
}
