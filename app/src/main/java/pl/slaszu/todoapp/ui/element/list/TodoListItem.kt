package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.utils.printStartDate

@Composable
fun TodoListItem(
    item: TodoModel,
    setting: Setting,
    onCheckItem: (Boolean) -> Unit,
    onEditItem: () -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    var alertDialog by remember { mutableStateOf(false) }

    // Używamy surfaceContainer dla karty, aby lekko odcinała się od background
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onEditItem() },
        color = if (item.done) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nowoczesny Toggle Checkbox
            CustomCheckbox(
                checked = item.done,
                onCheckedChange = { onCheckItem(it) }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (item.done) FontWeight.Normal else FontWeight.Medium,
                        textDecoration = if (item.done) TextDecoration.LineThrough else null,
                        color = if (item.done) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        else MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                TodoItemInfo(item = item, setting = setting)
            }

            IconButton(
                onClick = { alertDialog = true },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                )
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (alertDialog) {
        TodoDeleteConfirmAlertDialog(
            item = item,
            onConfirm = {
                onDeleteItem()
                alertDialog = false
            },
            onDismiss = { alertDialog = false }
        )
    }
}

@Composable
private fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    IconButton(onClick = { onCheckedChange(!checked) }) {
        Icon(
            imageVector = if (checked) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun TodoItemInfo(
    item: TodoModel,
    setting: Setting
) {
    if (item.startDate != null || item.repeatType != null) {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (item.startDate != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_access_time_filled_24),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.printStartDate(
                            stringResource(R.string.todo_item_no_date),
                            "${setting.reminderRepeatHour}:${setting.reminderRepeatMinute.toString().padStart(2, '0')}"
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (item.repeatType != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.repeatType!!.getTranslation(resource = LocalResources.current),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}