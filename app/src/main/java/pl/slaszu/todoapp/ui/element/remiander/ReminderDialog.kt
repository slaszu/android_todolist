package pl.slaszu.todoapp.ui.element.remiander

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun ReminderDialog(
    reminderItemsId: LongArray,
    items: List<TodoModel>,
    onDismiss: () -> Unit,
    onCloseItem: (TodoModel) -> Unit
) {
    val items = items.filter {
        reminderItemsId.contains(it.id)
    }

    if (items.isEmpty()) return


    val checkedIds = items.map { it.id }.toMutableSet()
    var checkedIdsCount by rememberSaveable { mutableStateOf(checkedIds.size) }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            LazyColumn {
                items(
                    items = items
                ) { todoItem ->
                    ReminderDialogItem(
                        item = todoItem,
                        onCheck = {
                            if (it) {
                                checkedIds.add(todoItem.id)
                            } else {
                                checkedIds.remove(todoItem.id)
                            }
                            checkedIdsCount = checkedIds.size
                            Log.d("myapp", checkedIds.joinToString())
                        }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                items.forEach {
                                    if (checkedIds.contains(it.id)) {
                                        onCloseItem(it)
                                    }
                                }
                                onDismiss()
                            },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text("Zakończ zadania (${checkedIdsCount})")
                        }
                    }

                }
            }
        }
    }

}

@Composable
fun ReminderDialogItem(
    item: TodoModel,
    onCheck: (Boolean) -> Unit
) {
    var checked by rememberSaveable { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = !checked
                onCheck(it)
            }
        )
        Text(text = item.text)
    }
    HorizontalDivider()
}

@Preview
@Composable
fun ReminderDialogPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            ReminderDialog(
                reminderItemsId = longArrayOf(1, 2),
                items = listOf(
                    FakeTodoModel(1, "fakowy item 1"),
                    FakeTodoModel(2, "fakowy item drugi !")
                ),
                onCloseItem = {},
                onDismiss = {}
            )
        }
    }
}