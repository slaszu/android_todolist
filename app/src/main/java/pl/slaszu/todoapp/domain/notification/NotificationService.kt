package pl.slaszu.todoapp.domain.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.slaszu.todoapp.MainActivity
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.TodoModel

class NotificationService(
    private val activity: Activity
) {
    companion object {
        const val CHANNEL_ID = "todoapp_channel_unique_identifier"
        const val CHANNEL_NAME = "TodoApp"
        const val NOTIFICATION_REQUEST_CODE = 1
    }

    private val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

    fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            activity,
            this.notificationPermission
        ) == PackageManager.PERMISSION_GRANTED

    fun openSettingActivity() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            //putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
        }
        activity.startActivity(intent)
    }

    fun askPermissionRequest() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(this.notificationPermission),
            NOTIFICATION_REQUEST_CODE
        )
    }

    fun isPermissionGranted(permissions: Array<String>, grantResults: IntArray): Boolean {
        permissions.forEachIndexed { index, s ->
            if (s == notificationPermission) {
                return grantResults[index] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(notification: Notification) {
        if (this.hasPermission()) {
            val notificationManagerCompat = NotificationManagerCompat.from(activity)
            notificationManagerCompat.notify(1, notification)
        }
    }

    fun buildNotification(todo: TodoModel): Notification {
        return buildNotification(todo.text, todo.id)
    }

    private fun buildNotification(text: String, uniqueId: Int? = null): Notification {
        Log.d("myapp", "buildNotification")
        // Create an explicit intent for an Activity in your app.
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        uniqueId?.let { uId ->
            intent.putExtra("uniqueId", uId)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            activity, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(activity, CHANNEL_ID)
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
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}