package pl.slaszu.todoapp.domain.reminder

interface ReminderPermissionService {
    fun hasPermission(): Boolean
}