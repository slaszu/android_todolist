package pl.slaszu.todoapp.domain.setting

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import pl.slaszu.todoapp.domain.reminder.repeat.ReminderRepeatService
import javax.inject.Inject

class SettingManager @Inject constructor(
    private val settingRepository: SettingRepository,
    private val reminderRepeatService: ReminderRepeatService
) {
    suspend fun updateSetting(setting: Setting, oldSetting: Setting? = null) {
        withContext(Dispatchers.IO) {
            settingRepository.saveData(setting)

            if (oldSetting?.reminderRepeatHour != setting.reminderRepeatHour ||
                oldSetting.reminderRepeatMinute != setting.reminderRepeatMinute
            ) {
                reminderRepeatService.scheduleRepeatOnePerDay(setting)
            }
        }
    }

    suspend fun bootstrap() {
        withContext(Dispatchers.IO) {
            val setting = settingRepository.getData().first()
            reminderRepeatService.scheduleRepeatOnePerDay(setting)
        }
    }
}