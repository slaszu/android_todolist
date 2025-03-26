package pl.slaszu.todoapp.infrastructure.reminder

import pl.slaszu.todoapp.domain.reminder.ReminderPermissionService

class FakeReminderPermissionServiceService : ReminderPermissionService {
    override fun hasPermission(): Boolean {
        return true
    }
}