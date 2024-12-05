package pl.slaszu.todoapp.infrastructure.reminder

import pl.slaszu.todoapp.domain.reminder.ReminderPermission

class FakeReminderPermissionService : ReminderPermission {
    override fun hasPermission(): Boolean {
        return true
    }

    override fun openSettingActivity() {
        // do nothing
    }
}