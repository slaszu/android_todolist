package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.TimelineHeader
import pl.slaszu.todoapp.domain.TodoModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoListTimeline(
    itemsGrouped: Map<TimelineHeader, List<TodoModel>>,
    setting: Setting,
    onCheck: (TodoModel, Boolean) -> Unit,
    onEdit: (TodoModel) -> Unit,
    onDelete: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        itemsGrouped.forEach { header, todoList ->
            stickyHeader {
                TodoListHeader(
                    header = header
                )
            }
            items(
                items = todoList
            ) { todoItem ->
                TodoListItem(
                    item = todoItem,
                    setting = setting,
                    onCheckItem = { checked -> onCheck(todoItem, checked) },
                    onEditItem = { onEdit(todoItem) },
                    onDeleteItem = { onDelete(todoItem) },
                    modifier = modifier
                )

            }
        }
    }
}

@Composable
fun TodoListHeader(
    header: TimelineHeader
) {
    var color = MaterialTheme.colorScheme.primary
    if (header == TimelineHeader.OUT_OF_DATE) {
        color = MaterialTheme.colorScheme.error
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            color = color,
            fontWeight = FontWeight.Bold,
            text = stringResource(header.translationResourceKey)
        )
    }
    HorizontalDivider(
        thickness = 2.dp,
        color = color,
    )
}