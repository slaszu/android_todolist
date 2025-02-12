package pl.slaszu.todoapp.ui.element.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.utils.clearTime
import pl.slaszu.todoapp.domain.utils.isTimeSet
import pl.slaszu.todoapp.domain.utils.printDate
import pl.slaszu.todoapp.domain.utils.printTime
import pl.slaszu.todoapp.domain.utils.setTime
import pl.slaszu.todoapp.domain.utils.toEpochMillis
import pl.slaszu.todoapp.domain.utils.toLocalDateTime
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoForm(
    item: TodoModel?,
    onSave: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {
    if (item == null) {
        Text("Loading...")
        return
    }
    val currentTime = LocalDateTime.now()

    var text by remember { mutableStateOf(item.text) }
    var done by remember { mutableStateOf(item.done) }
    var chooseDateDialog by remember { mutableStateOf(false) }
    var chooseTimeDialog by remember { mutableStateOf(false) }

    var todoLocalDateTime by rememberSaveable { mutableStateOf(item.startDate) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todoLocalDateTime?.toEpochMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= currentTime.clearTime().toEpochMillis()
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= currentTime.year
            }
        }
    )
    val currentTimePicker = currentTime.plusMinutes(15)
    val timePickerState = rememberTimePickerState(
        initialHour = todoLocalDateTime?.hour ?: currentTimePicker.hour,
        initialMinute = todoLocalDateTime?.minute ?: currentTimePicker.minute,
        is24Hour = true,
    )




    Column {

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = {
                    Text(stringResource(R.string.todo_form_text_label))
                },
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clickable { chooseDateDialog = true }
        ) {

            Text(
                text = todoLocalDateTime?.printDate()
                    ?: stringResource(R.string.todo_form_no_date)
            )
            IconButton(
                onClick = { chooseDateDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Choose date"
                )
            }
            if (datePickerState.selectedDateMillis != null) {
                IconButton(
                    onClick = { todoLocalDateTime = null }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear date"
                    )
                }
            }

            if (chooseDateDialog) {
                DatePickerModal(
                    datePickerState = datePickerState,
                    onDismiss = { chooseDateDialog = false },
                    onDateSelected = { millis ->
                        todoLocalDateTime = millis?.toLocalDateTime()?.clearTime()
                    }
                )
            }
        }


        if (todoLocalDateTime != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clickable { chooseTimeDialog = true }
            ) {

                Text(
                    text = todoLocalDateTime?.printTime()
                        ?: stringResource(R.string.todo_form_no_time)
                )
                IconButton(
                    onClick = { chooseTimeDialog = true }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_access_time_filled_24),
                        contentDescription = "Choose date"
                    )
                }
                if (todoLocalDateTime?.isTimeSet() == true) {
                    IconButton(
                        onClick = { todoLocalDateTime = todoLocalDateTime?.clearTime() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear date"
                        )
                    }
                }

                if (chooseTimeDialog) {
                    TimeDialogModel(
                        timePickerState = timePickerState,
                        onDismiss = { chooseTimeDialog = false },
                        onConfirm = {
                            todoLocalDateTime = todoLocalDateTime?.setTime(it.hour, it.minute)
                        }
                    )
                }
            }
        }




//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.End,
//            modifier = Modifier
//                .padding(10.dp)
//                .fillMaxWidth()
//        ) {
//
//            Text(
//                text = stringResource(R.string.todo_form_is_finished),
//                modifier = Modifier.padding(10.dp)
//            )
//            Switch(
//                checked = done,
//                onCheckedChange = {
//                    done = it
//                }
//            )
//
//        }


        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Button(
                onClick = {
                    onSave(
                        item.copy(
                            "text" to text,
                            "done" to done,
                            "startDate" to todoLocalDateTime
                        )
                    )
                },
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
                item = FakeTodoModel(
                    text = "",
                    startDate = LocalDateTime.now().clearTime()
                ),
                onSave = { item -> println(item) },
                modifier = Modifier.padding(it)
            )
        }
    }
}