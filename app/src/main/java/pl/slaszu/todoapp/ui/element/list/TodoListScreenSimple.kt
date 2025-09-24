package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.TimelineHeader
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.todo.FakeTodoModel
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import java.time.LocalDateTime

@Composable
fun TodoListScreenSimple(
    timelineItemList: Map<TimelineHeader, List<TodoModel>> = emptyMap(),
    setting: Setting,
    onCheck: (TodoModel, Boolean) -> Unit,
    onEdit: (TodoModel) -> Unit,
    onDelete: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {

    TodoListTimeline(
        itemsGrouped = timelineItemList,
        setting = setting,
        onCheck = onCheck,
        onEdit = onEdit,
        onDelete = onDelete,
        modifier = modifier
    )


}

@Preview
@Composable
fun TodoListScreenSimplePreview() {

    TodoAppTheme {
        Scaffold() { it ->
            TodoListScreenSimple(
                timelineItemList = List(10) { i ->
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
                modifier = Modifier.padding(it)
            )
        }
    }
}