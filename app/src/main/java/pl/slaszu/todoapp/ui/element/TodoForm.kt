package pl.slaszu.todoapp.ui.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun TodoForm(
    item: TodoModel,
    onSave: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val text by remember { mutableStateOf(item.text ?: "") }
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            label = {
                Text(stringResource(R.string.todo_form_text_label))
            },
            modifier = Modifier
                .height(150.dp)
                .padding(10.dp)
                .fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Button(
                onClick = { onSave(item.copy(text = text)) },
            ) {
                Text(stringResource(R.string.todo_form_save_btn))
            }
        }
    }
}

@Preview
@Composable
fun TodoFormPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            TodoForm(
                item = TodoModel(
                    text = ""
                ),
                onSave = { item -> println(item) },
                modifier = Modifier.padding(it)
            )
        }
    }
}