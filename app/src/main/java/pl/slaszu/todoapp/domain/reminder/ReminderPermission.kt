package pl.slaszu.todoapp.domain.reminder

interface ReminderPermission {
    fun hasPermission(): Boolean

    fun openSettingActivity()
}