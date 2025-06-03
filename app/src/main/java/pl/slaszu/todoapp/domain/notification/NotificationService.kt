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
import pl.slaszu.todoapp.domain.todo.TodoModel
import java.util.Random

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
            notificationManagerCompat.notify(Random().nextInt(), this.buildNotification(items))
        }
    }

    private fun buildNotification(items: Array<TodoModel>): Notification {
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
            .build()
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