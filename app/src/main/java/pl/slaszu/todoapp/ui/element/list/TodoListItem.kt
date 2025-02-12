package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.utils.printStartDate
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import pl.slaszu.todoapp.ui.theme.TodoAppTypography
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEditItem()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Checkbox(
            checked = item.done,
            onCheckedChange = { checked ->
                onCheckItem(checked)
            },
            modifier = modifier.weight(0.1f)
        )

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
            if (item.startDate != null) {
                Text(
                    text = item.printStartDate(
                        stringResource(R.string.todo_item_no_date),
                        stringResource(R.string.todo_item_no_time)
                    ),
                    fontSize = TextUnit(3f, TextUnitType.Em)
                )
                if (!setting.notificationAllowed) {
                    Text(
                        text = stringResource(R.string.notification_disabled),
                        style = TodoAppTypography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
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

@Preview
@Composable
fun TodoListItemPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            TodoListItem(
                item = FakeTodoModel(
                    text = "Jakieś tam przypomnienie, które jest testowe",
                    startDate = LocalDateTime.now()
                ),
                setting = Setting(

                ),
                onCheckItem = {},
                onEditItem = {},
                onDeleteItem = {}
            )
        }
    }
}