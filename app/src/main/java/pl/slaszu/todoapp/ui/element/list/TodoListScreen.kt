package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
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
    doneTimelineList: Map<TimelineHeader, List<TodoModel>> = emptyMap(),
    setting: Setting,
    onCheck: (TodoModel, Boolean) -> Unit,
    onEdit: (TodoModel) -> Unit,
    onDelete: (TodoModel) -> Unit,
    tabSelectedRemember: TodoItemType,
    onTabChange: (TodoItemType) -> Unit,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = tabSelectedRemember.ordinal,
        pageCount = { TodoItemType.entries.size }
    )

    val onTabChangeWithScroll: (TodoItemType) -> Unit = {
        onTabChange(it)
        scope.launch {
            pagerState.animateScrollToPage(it.ordinal)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onTabChange(TodoItemType.entries[page])
        }
    }

    Column {
        TabRow(
            selectedTabIndex = tabSelectedRemember.index,
        ) {
            MyTab(
                type = TodoItemType.TIMELINE,
                quantity = timelineItemList.map {
                    it.value.size
                }.sum(),
                selectedType = tabSelectedRemember,
                onTabChange = onTabChangeWithScroll
            )

            MyTab(
                type = TodoItemType.GENERAL,
                quantity = generalItemList.size,
                selectedType = tabSelectedRemember,
                onTabChange = onTabChangeWithScroll
            )

            MyTab(
                type = TodoItemType.DONE,
                quantity = doneTimelineList.map {
                    it.value.size
                }.sum(),
                selectedType = tabSelectedRemember,
                onTabChange = onTabChangeWithScroll
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.background
        )

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxHeight()
        ) { page ->

            when {
                TodoItemType.entries[page] == TodoItemType.TIMELINE -> TodoListTimeline(
                    itemsGrouped = timelineItemList,
                    setting = setting,
                    onCheck = onCheck,
                    onEdit = onEdit,
                    onDelete = onDelete,
                    modifier = modifier
                )

                TodoItemType.entries[page] == TodoItemType.DONE -> TodoListTimeline(
                    itemsGrouped = doneTimelineList,
                    setting = setting,
                    onCheck = onCheck,
                    onEdit = onEdit,
                    onDelete = onDelete,
                    modifier = modifier
                )

                TodoItemType.entries[page] == TodoItemType.GENERAL -> TodoList(
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Badge(
                    modifier = Modifier.padding(5.dp, 2.dp)
                ) {
                    Text(
                        text = quantity.toString()
                    )
                }
                Text(
                    text = stringResource(type.translationResourceKey)
                )
            }
        },
        selected = type == selectedType,
        onClick = { onTabChange(type) }
    )
}


@Preview
@Composable
fun TodoListScreenPreview() {

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
                    PresentationService.convertToTimelineMap(it)
                },
                doneTimelineList = List(5) { i ->
                    FakeTodoModel(
                        text = "Timeline item nr $i",
                        startDate = LocalDateTime.now().plusDays(i.toLong() * 2)
                    )
                }.let {
                    PresentationService.convertToTimelineMap(it)
                },
                setting = Setting(),
                onCheck = { _, _ -> },
                onEdit = {},
                onDelete = {},
                tabSelectedRemember = TodoItemType.TIMELINE,
                onTabChange = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}