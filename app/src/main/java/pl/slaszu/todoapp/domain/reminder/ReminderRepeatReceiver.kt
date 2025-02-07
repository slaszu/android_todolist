package pl.slaszu.todoapp.domain.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import pl.slaszu.todoapp.domain.notification.NotificationService
import pl.slaszu.todoapp.domain.utils.clearTime
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class ReminderRepeatReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: TodoRepository<TodoModel>

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("myapp", "Receiver")

        if (context == null || intent == null) {
            Log.d("myapp", "Receiver: early return")
            return
        }

        val notificationService = NotificationService(context)

        runBlocking {
            val itemArray = repository.getByDate(LocalDateTime.now().clearTime())
            Log.d("myapp", "Receiver: notification send: $itemArray")

            if (itemArray.isEmpty()) return@runBlocking

            notificationService.sendNotification(itemArray)

            Log.d("myapp", "Receiver: coroutine done")
        }
    }
}