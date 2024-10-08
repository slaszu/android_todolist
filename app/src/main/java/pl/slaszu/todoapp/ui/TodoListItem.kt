package pl.slaszu.todoapp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun TodoListItem(
    text: String,
    modifier: Modifier = Modifier,
    onCheck: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text, modifier = modifier
            .weight(1f)
            .padding(start = 16.dp))
        Spacer(modifier = modifier)
        IconButton(
            onClick = onCheck
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "check"
            )
        }
    }
}

@Preview
@Composable
fun TodoListItemPreview() {
    TodoAppTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            var text by remember { mutableStateOf("First item") }

            TodoListItem(
                text = text,
                onCheck = { text = "bleee" }
            )
        }
    }
}