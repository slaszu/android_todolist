package pl.slaszu.todoapp.domain.todo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.slaszu.todoapp.domain.reminder.exact.ReminderExactService
import javax.inject.Inject

class TodoManager @Inject constructor(
    private val reminderExactService: ReminderExactService,
    private val todoRepository: TodoRepository<TodoModel>
) {
    suspend fun save(item: TodoModel) {
        withContext(Dispatchers.IO) {
            todoRepository.save(item)
            reminderExactService.schedule(item)
        }
    }

    suspend fun delete(item: TodoModel) {
        withContext(Dispatchers.IO) {
            todoRepository.delete(item)
            reminderExactService.cancel(item)
        }
    }
}