package pl.slaszu.todoapp.domain.todo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.slaszu.todoapp.domain.backup.BackupManager
import pl.slaszu.todoapp.domain.reminder.exact.ReminderExactService
import javax.inject.Inject

class TodoManager @Inject constructor(
    private val reminderExactService: ReminderExactService,
    private val todoRepository: TodoRepository<TodoModel>,
    private val backManager: BackupManager
) {
    suspend fun save(item: TodoModel): TodoModel =
        withContext(Dispatchers.IO) {
            val savedItem = todoRepository.save(item)
            reminderExactService.schedule(savedItem)
            backManager.addOrUpdateItem(item)
            return@withContext savedItem
        }


    suspend fun delete(item: TodoModel) =
        withContext(Dispatchers.IO) {
            todoRepository.delete(item)
            reminderExactService.cancel(item)
            backManager.deleteItem(item)
        }

}