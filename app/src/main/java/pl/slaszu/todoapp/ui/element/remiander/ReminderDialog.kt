package pl.slaszu.todoapp.ui.element.remiander

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import pl.slaszu.todoapp.domain.repeat.RepeatType
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun ReminderDialog(
    items: List<TodoModel>,
    onDismiss: () -> Unit,
    onCloseItem: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {

    if (items.isEmpty()) return

    var selectedIds by rememberSaveable { mutableStateOf(items.map { it.id }.toSet()) }

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
                                selectedIds = selectedIds.plus(todoItem.id)
                            } else {
                                selectedIds = selectedIds.minus(todoItem.id)
                            }
                            Log.d("myapp", selectedIds.joinToString())
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
                                    if (selectedIds.contains(it.id)) {
                                        onCloseItem(it)
                                    }
                                }
                                onDismiss()
                            },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text("Zakończ zadania (${selectedIds.size})")
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


        Column {
            Row {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = !checked
                        onCheck(it)
                    }
                )
                Text(
                    text = item.text,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

//            if (item.repeatType != null) {
//                Row(
//                    horizontalArrangement = Arrangement.End,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .align(Alignment.End)
//                ) {
//                    Text(
//                        text = stringResource(item.repeatType?.translationKey ?: 0),
//                        fontSize = TextUnit(3f, TextUnitType.Em)
//                    )
//                    Icon(
//                        Icons.Filled.Refresh,
//                        contentDescription = "Repeat type",
//                        modifier = Modifier.size(16.dp)
//                    )
//                }
//            }
        }

    }
    HorizontalDivider()
}

@Preview
@Composable
fun ReminderDialogPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            ReminderDialog(
                items = listOf(
                    FakeTodoModel(
                        1,
                        "fakowy item o jakimś przypomnieniu",
                        repeatType = RepeatType.RepeatTypeWeek()
                    ),
                    FakeTodoModel(
                        2,
                        "inne krótkie przypomnienie",
                        repeatType = RepeatType.RepeatTypeYear()
                    )
                ),
                onCloseItem = {},
                onDismiss = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}