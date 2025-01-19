package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.utils.printStartDate
import pl.slaszu.todoapp.ui.theme.Typography

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
            .padding(5.dp),
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
                .padding(start = 16.dp)
        ) {
            Text(
                text = item.text,
                textDecoration = TextDecoration.LineThrough.takeIf { item.done },
            )
            Text(
                text = item.printStartDate("No date", "Time not set"),
                style = Typography.labelSmall,
            )
            if (item.startDate != null && !setting.notificationAllowed) {
                Text(
                    text = "Notifications not allowed by user !",
                    style = Typography.labelSmall,
                    color = Color.Gray
                )
            }
        }

        IconButton(
            onClick = { onEditItem() }
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Edit item"
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