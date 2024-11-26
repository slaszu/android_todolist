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
import pl.slaszu.todoapp.domain.TodoModel

class NotificationService(
    private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "todoapp_channel_unique_identifier"
        const val CHANNEL_NAME = "TodoApp"
    }

    fun sendNotification(item: TodoModel) {

        requireNotNull(item.id)
        requireNotNull(item.startDate)


        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (notificationManagerCompat.areNotificationsEnabled()) {

            notificationManagerCompat.notify(1, this.buildNotification(item))
        }
    }

    private fun buildNotification(item: TodoModel): Notification {

        Log.d("myapp", "buildNotification")

        requireNotNull(item.id)
        requireNotNull(item.startDate)

        // Create an explicit intent for an Activity in your app.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("ITEM_ID", item.id)
        }


        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_access_time_filled_24)
            .setContentTitle("TodoApp")
            .setContentText(item.text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
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