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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.TimelineHeader
import pl.slaszu.todoapp.domain.TodoItemType
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
    tabIndex: TodoItemType,
    onTabChange: (TodoItemType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        TabRow(
            selectedTabIndex = tabIndex.index,
        ) {
            MyTab(
                type = TodoItemType.TIMELINE,
                quantity = timelineItemList.map {
                    it.value.size
                }.sum(),
                selectedType = tabIndex,
                onTabChange = onTabChange
            )

            MyTab(
                type = TodoItemType.GENERAL,
                quantity = generalItemList.size,
                selectedType = tabIndex,
                onTabChange = onTabChange
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.background
        )

        if (tabIndex == TodoItemType.TIMELINE) {
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

@Composable
private fun MyTab(
    type: TodoItemType,
    selectedType: TodoItemType,
    quantity: Int,
    onTabChange: (TodoItemType) -> Unit
) {
    Tab(
        text = {
            Row {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = "General icon",
                    modifier = Modifier.padding(5.dp, 0.dp)
                )
                Text(
                    text = stringResource(type.translationResourceKey)
                )
                Badge(
                    modifier = Modifier.padding(5.dp, 2.dp)
                ) {
                    Text(
                        text = quantity.toString()
                    )
                }
            }
        },
        selected = type == selectedType,
        onClick = { onTabChange(type) }
    )
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
                tabIndex = TodoItemType.TIMELINE,
                onTabChange = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}