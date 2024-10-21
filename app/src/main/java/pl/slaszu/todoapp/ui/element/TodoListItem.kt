package pl.slaszu.todoapp.ui.element

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun TodoListItem(
    item: TodoModel,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.text,
            modifier = modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Checkbox(
            checked = item.done,
            onCheckedChange = { checked ->
                onCheckedChange(checked)
            },
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun TodoListItemPreview() {
    TodoAppTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            TodoListItem(
                item = TodoModel(1,"testowy model"),
                onCheckedChange = {}
            )
        }
    }
}