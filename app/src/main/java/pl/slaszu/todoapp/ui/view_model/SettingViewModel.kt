package pl.slaszu.todoapp.ui.view_model

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.backup.BackupManager
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.setting.SettingManager
import pl.slaszu.todoapp.domain.setting.SettingRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingManager: SettingManager,
    private val settingRepository: SettingRepository,
    private val backupManager: BackupManager
) : ViewModel() {

    val settingFlow = settingRepository.getData()

    fun saveSetting(setting: Setting, oldSetting: Setting? = null) {
        this.viewModelScope.launch {
            settingManager.updateSetting(setting, oldSetting)
        }
    }

    fun setupBackup(snackbarHostState: SnackbarHostState) {
        this.backupManager.importAll()
    }
}