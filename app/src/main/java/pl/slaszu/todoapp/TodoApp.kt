package pl.slaszu.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import pl.slaszu.todoapp.domain.notification.NotificationService

@HiltAndroidApp
class TodoApp : Application() {

    private val notificationService = NotificationService(this)

    override fun onCreate() {
        super.onCreate()

        notificationService.createNotificationChannel()
    }
}