package pl.slaszu.todoapp.domain.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.slaszu.todoapp.MainActivity
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.notification.action.NotificationFinishActionReceiver
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.utils.getUniqueInt

class NotificationService(
    private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "todoapp_channel_unique_identifier"
        const val CHANNEL_NAME = "TodoApp"
    }

    fun sendNotification(item: TodoModel) {
        this.sendNotification(arrayOf(item))
    }

    fun sendNotification(items: Array<TodoModel>) {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (notificationManagerCompat.areNotificationsEnabled()) {
            val notificationId = items.getUniqueInt()
            notificationManagerCompat.notify(
                notificationId, this.buildNotification(
                    items = items,
                    finishIntend = this.getFinishAction(items, notificationId)
                )
            )
        }
    }

    private fun buildNotification(
        items: Array<TodoModel>,
        finishIntend: PendingIntent
    ): Notification {
        Log.d("myapp", "buildNotification")

        // Create an explicit intent for an Activity in your app.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val (text, title) = this.prepareTextForNotification(items)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_access_time_filled_24)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup("todo_app_notification_group")
            .addAction(
                R.drawable.baseline_access_time_filled_24,
                "Done",
                finishIntend
            )
            .build()
    }

    private fun getFinishAction(items: Array<TodoModel>, notificationId: Int): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, NotificationFinishActionReceiver::class.java).apply {
                this.putExtra(
                    NotificationFinishActionReceiver.ITEMS_EXTRAS,
                    items.map { it.id }.toTypedArray()
                )
                this.putExtra(
                    NotificationFinishActionReceiver.NOTIFICATION_ID_EXTRAS,
                    notificationId
                )
            },
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun prepareTextForNotification(items: Array<TodoModel>): Pair<String, String> {
        if (items.size == 1) {
            return Pair(items.first().text, "TodoApp")
        }

        return Pair(
            items.mapIndexed { index, todoModel ->
                "${index + 1}. ${
                    todoModel.text
                }"
            }.joinToString(
                separator = "\n"
            ), "TodoApp (${items.size})"
        )
    }

    fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}