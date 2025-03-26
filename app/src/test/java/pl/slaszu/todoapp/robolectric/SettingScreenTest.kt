package pl.slaszu.todoapp.robolectric

import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.ui.element.setting.SettingScreen

@RunWith(RobolectricTestRunner::class)
class SettingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingScreenTest() {
        composeTestRule.setContent {
            SettingScreen(
                setting = Setting(
                    notificationAllowed = true,
                    reminderAllowed = false,
                    reminderRepeatHour = 10,
                    reminderRepeatMinute = 23
                ),
                onChange = {},
                onNotificationClick = {},
                onReminderClick = {}
            )
        }


        composeTestRule.onNodeWithTag("notification").assertIsToggleable().assertIsOn()
        composeTestRule.onNodeWithTag("reminder").assertIsToggleable().assertIsOff()
        composeTestRule.onNodeWithText("10:23").assertExists()

        composeTestRule.onNodeWithContentDescription("time_picker").assertDoesNotExist()
        composeTestRule.onNodeWithTag("repeat_time").performClick()
        composeTestRule.onNodeWithContentDescription("time_picker").assertExists()
    }
}