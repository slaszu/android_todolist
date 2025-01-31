package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.TimelineHeader
import pl.slaszu.todoapp.domain.TodoModel

@Composable
fun TodoListScreen(
    generalItemList: List<TodoModel> = emptyList(),
    timelineItemList: Map<TimelineHeader, List<TodoModel>> = emptyMap(),
    setting: Setting,
    onCheck: (TodoModel, Boolean) -> Unit,
    onEdit: (TodoModel) -> Unit,
    onDelete: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {

    var tabIndex by remember { mutableIntStateOf(0) }

    Column {
        TabRow(
            selectedTabIndex = tabIndex,
        ) {
            Tab(
                text = {
                    Row {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = "General icon",
                            modifier = Modifier.padding(5.dp, 0.dp)
                        )
                        Text("Timeline")
                    }
                },
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 }
            )
            Tab(
                text = {
                    Row {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = "General icon",
                            modifier = Modifier.padding(5.dp, 0.dp)
                        )
                        Text("Other")
                    }
                },
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 }
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.background
        )

        if (tabIndex == 0) {
            TodoListTimeline(
                itemsGrouped = timelineItemList,
                setting = setting,
                onCheck = onCheck,
                onEdit = onEdit,
                onDelete = onDelete,
                modifier = modifier
            )
        } else {
            TodoList(
                items = generalItemList,
                setting = setting,
                onCheck = onCheck,
                onEdit = onEdit,
                onDelete = onDelete,
                modifier = modifier
            )
        }



    }
}

//@Preview
//@Composable
//fun TodoListScreenPreview() {
//    TodoAppTheme {
//        Scaffold() { it ->
//            TodoListScreen(
//                generalItemList = List(5) { i ->
//                    FakeTodoModel(text = "General item nr $i", startDate = LocalDateTime.now())
//                },
//                timelineItemList = List(2) { i ->
//                    FakeTodoModel(text = "Timeline item nr $i", startDate = LocalDateTime.now())
//                },
//                setting = Setting(),
//                onCheck = { _, _ -> },
//                onEdit = {},
//                onDelete = {},
//                modifier = Modifier.padding(it)
//            )
//        }
//    }
//}