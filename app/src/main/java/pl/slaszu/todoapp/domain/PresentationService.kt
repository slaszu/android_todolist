package pl.slaszu.todoapp.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

class PresentationService @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun getStringResource(key: Int): String {
        return this.context.getString(key)
    }

    companion object {
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
}
