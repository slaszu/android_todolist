package pl.slaszu.todoapp.robolectric

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.ui.element.setting.SettingScreen

@RunWith(AndroidJUnit4::class)
class SettingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingScreenTest() {
        composeTestRule.setContent {
            SettingScreen(
                setting = Setting(),
                onChange = {},
                onNotificationClick = {},
                onReminderClick = {}
            )
        }
    }
}