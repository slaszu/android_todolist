package pl.slaszu.todoapp.ui.element.form

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pl.slaszu.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {

                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_back))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

//@Preview
//@Composable
//fun DatePickerModalPreview() {
//    TodoAppTheme {
//        Scaffold() { it ->
//            DatePickerModal(
//                onDateSelected = {},
//                onDismiss = {},
//                modifier = Modifier.padding(it)
//            )
//        }
//    }
//}