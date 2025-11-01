package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.repeat.RepeatType
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.todo.FakeTodoModel
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.utils.printStartDate
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import java.time.LocalDateTime

@Composable
fun TodoListItem(
    item: TodoModel,
    setting: Setting,
    onCheckItem: (Boolean) -> Unit,
    onEditItem: () -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {

    var alertDialog by remember { mutableStateOf(false) }
    var itemChecked by remember { mutableStateOf(item.done) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEditItem()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        ToggleIconButton(
            checked = itemChecked,
            onChange = { checked ->
                itemChecked = checked
                onCheckItem(checked)
            }
        )

//        Checkbox(
//            checked = item.done,
//            onCheckedChange = { checked ->
//                onCheckItem(checked)
//            },
//            modifier = modifier.weight(0.1f)
//        )

        Column(
            modifier = modifier
                .weight(0.9f)
                .padding(start = 16.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
        ) {
            Text(
                text = item.text,
                textDecoration = TextDecoration.LineThrough.takeIf { item.done },
                fontSize = TextUnit(4f, TextUnitType.Em)
            )
            TodoItemInfo(
                item = item,
                setting = setting
            )
        }

        IconButton(
            onClick = { alertDialog = true }
        ) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Delete item"
            )
        }
    }

    if (alertDialog) {
        TodoDeleteConfirmAlertDialog(
            item = item,
            onConfirm = {
                onDeleteItem()
                alertDialog = false
            },
            onDismiss = {
                alertDialog = false
            }
        )
    }

    HorizontalDivider()
}

@Composable
private fun ToggleIconButton(
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    IconButton(
        onClick = {
            onChange(!checked)
        }
    ) {
        Icon(
            imageVector = if (!checked) Icons.Filled.Check else Icons.Filled.CheckBox,
            contentDescription = if (checked) "Selected icon button" else "Unselected icon button."
        )
    }
}

@Composable
private fun TodoItemInfo(
    item: TodoModel,
    setting: Setting
) {
    if (item.startDate != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.8f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_access_time_filled_24),
                    contentDescription = "Choose date",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = item.printStartDate(
                        stringResource(R.string.todo_item_no_date),
                        "${setting.reminderRepeatHour}:${
                            setting.reminderRepeatMinute.toString().padStart(2, '0')
                        }"
                    ),
                    fontSize = TextUnit(3f, TextUnitType.Em)
                )
            }

            if (item.repeatType != null) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = item.repeatType!!.getTranslation(resource = LocalResources.current),
                        fontSize = TextUnit(3f, TextUnitType.Em)
                    )
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "Repeat type",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TodoListItemPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            TodoListItem(
                item = FakeTodoModel(
                    text = "Jakieś tam przypomnienie, które jest testowe",
                    startDate = LocalDateTime.now(),
                    repeatType = RepeatType.RepeatTypeWeek()
                ),
                setting = Setting(

                ),
                onCheckItem = {},
                onEditItem = {},
                onDeleteItem = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}