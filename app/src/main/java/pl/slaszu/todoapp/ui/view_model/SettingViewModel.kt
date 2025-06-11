package pl.slaszu.todoapp.ui.view_model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.backup.BackupManager
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.setting.SettingManager
import pl.slaszu.todoapp.domain.setting.SettingRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingManager: SettingManager,
    private val settingRepository: SettingRepository,
    private val backupManager: BackupManager,
    private val presentationService: PresentationService
) : ViewModel() {

    val settingFlow = settingRepository.getData()

    fun saveSetting(setting: Setting, oldSetting: Setting? = null) {
        this.viewModelScope.launch {
            settingManager.updateSetting(setting, oldSetting)
        }
    }

    fun setupBackup(snackbarHostState: SnackbarHostState) {
        this.viewModelScope.launch {
            backupManager.importAll()
            backupManager.exportAll()
            snackbarHostState.showSnackbar(
                message = presentationService.getStringResource(R.string.backup_done),
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
        }
    }
}