package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.slaszu.todoapp.domain.TimelineHeader
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.todo.TodoModel

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
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        // Dodajemy padding na dole, żeby ostatni element nie chował się za przyciskiem FAB
        contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp)
    ) {
        itemsGrouped.forEach { (header, todoList) ->
            stickyHeader(
                key = header.toString()
            ) {
                TodoListHeader(header = header)
            }

            items(
                items = todoList,
                key = { it.id } // Lepiej użyć ID modelu niż hashCode()
            ) { todoItem ->
                TodoListItem(
                    item = todoItem,
                    setting = setting,
                    onCheckItem = { checked -> onCheck(todoItem, checked) },
                    onEditItem = { onEdit(todoItem) },
                    onDeleteItem = { onDelete(todoItem) },
                    // Dodajemy padding między elementami
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Dodatkowy odstęp po każdej sekcji
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TodoListHeader(
    header: TimelineHeader
) {
    val isError = header == TimelineHeader.OUT_OF_DATE
    val mainColor =
        if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                // Efekt lekkiego przejścia tonalnego pod nagłówkiem
                Brush.verticalGradient(
                    0.8f to backgroundColor,
                    1.0f to Color.Transparent
                )
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mała kropka/ozdobnik przed tekstem dla nowoczesnego sznytu
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color = mainColor, shape = MaterialTheme.shapes.small)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(header.translationResourceKey).uppercase(),
                style = MaterialTheme.typography.labelLarge,
                color = mainColor,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp // Większy odstęp między literami w nagłówkach sekcji
            )
        }

        // Zamiast grubego HorizontalDivider, dajemy bardzo cienki,
        // niepełny pasek, który wygląda lżej
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.15f) // Pasek tylko na 15% szerokości
                .height(2.dp)
                .background(
                    color = mainColor.copy(alpha = 0.4f),
                    shape = MaterialTheme.shapes.small
                )
        )
    }
}