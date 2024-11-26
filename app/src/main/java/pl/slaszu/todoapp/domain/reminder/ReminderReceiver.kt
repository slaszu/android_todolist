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
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: TodoRepository<TodoModel>

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("myapp", "Receiver")

        if (context == null || intent == null) {
            Log.d("myapp", "Receiver: early return")
            return
        }

        val notificationService = NotificationService(context)
        val itemId = intent.getLongExtra("ITEM_ID", 0)

        runBlocking {
            repository.getById(itemId).collect { item ->
                if (item != null) {
                    Log.d("myapp", "Receiver: notification send")
                    notificationService.sendNotification(item)
                }
                Log.d("myapp", "Receiver: flow canceled")
            }
        }

    }
}