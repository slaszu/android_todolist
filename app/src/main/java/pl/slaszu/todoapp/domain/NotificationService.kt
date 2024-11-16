package pl.slaszu.todoapp.domain

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.slaszu.todoapp.MainActivity
import pl.slaszu.todoapp.R
import javax.inject.Inject

class NotificationService @Inject constructor(
    private val activity: Activity
) {
    private val channelId = "todoapp_channel_unique_identifier"
    private val channelName = "TodoApp"

    fun sendNotification(notification: Notification) {
        Log.d("myapp", "sendNotification")
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("myapp", "No premisions to notifi !")
        }

        val notificationManagerCompat = NotificationManagerCompat.from(activity)
        notificationManagerCompat.notify(1, notification)
        Log.d("myapp", "Notification send")
    }

    fun buildNotification(text: String): Notification {
        Log.d("myapp", "buildNotification")
        // Create an explicit intent for an Activity in your app.
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            activity, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(activity, this.channelId)
            .setSmallIcon(R.drawable.baseline_access_time_filled_24)
            .setContentTitle("TodoApp")
            .setContentText(text)
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
        val channel = NotificationChannel(this.channelId, this.channelName, importance)
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}