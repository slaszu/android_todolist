package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.TimelineHeader
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import java.time.LocalDateTime

@Composable
fun TodoListScreen(
    generalItemList: List<TodoModel> = emptyList(),
    timelineItemList: Map<TimelineHeader, List<TodoModel>> = emptyMap(),
    setting: Setting,
    onCheck: (TodoModel, Boolean) -> Unit,
    onEdit: (TodoModel) -> Unit,
    onDelete: (TodoModel) -> Unit,
    tabIndex: Int = 0,
    onTabChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
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
                        Badge(
                            modifier = Modifier.padding(5.dp, 2.dp)
                        ) {
                            Text("8")
                        }
                    }
                },
                selected = tabIndex == 0,
                onClick = { onTabChange(0) }
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
                onClick = { onTabChange(1) }
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

private fun MyTab() {

}

@Preview
@Composable
fun TodoListScreenPreview() {
    val presentationService = PresentationService()

    TodoAppTheme {
        Scaffold() { it ->
            TodoListScreen(
                generalItemList = List(5) { i ->
                    FakeTodoModel(text = "General item nr $i")
                },
                timelineItemList = List(10) { i ->
                    FakeTodoModel(
                        text = "Timeline item nr $i",
                        startDate = LocalDateTime.now().plusDays(i.toLong() * 2)
                    )
                }.let {
                    presentationService.convertToTimelineMap(it)
                },
                setting = Setting(),
                onCheck = { _, _ -> },
                onEdit = {},
                onDelete = {},
                tabIndex = 0,
                onTabChange = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}