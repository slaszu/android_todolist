package pl.slaszu.todoapp.domain.reminder.exact

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.todo.TodoRepository
import java.time.LocalDateTime
import javax.inject.Inject

class ReminderExactManager @Inject constructor(
    private val todoRepository: TodoRepository<TodoModel>,
    private val reminderExactService: ReminderExactService
) {
    suspend fun bootstrap(now: LocalDateTime = LocalDateTime.now()) {
        withContext(Dispatchers.IO) {
            Log.d("myapp","Reminder bootstrap")
            todoRepository.getOnlyActiveTimeline().forEach { todoItem ->
                if (todoItem.startDate != null && todoItem.startDate!! > now ) {
                    reminderExactService.schedule(todoItem)
                }
            }
        }
    }
}