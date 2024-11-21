package pl.slaszu.todoapp.domain.notification

import android.app.Notification

class TodoNotification: Notification() {

    var uniqueId: Int
        get() = this.extras.getInt("uniqueId")
        set(value) = this.extras.putInt("uniqueId", value)
}