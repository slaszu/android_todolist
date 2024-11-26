package pl.slaszu.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApp:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}