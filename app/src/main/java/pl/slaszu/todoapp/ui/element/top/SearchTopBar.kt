package pl.slaszu.todoapp.ui.element.top

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    onCancelClick: () -> Unit
) {

    var text by rememberSaveable { mutableStateOf("") }

    TopAppBar(
        modifier = Modifier.padding(5.dp),
        title = {
            OutlinedTextField(
                leadingIcon = {
                    IconButton(
                        onClick = onCancelClick
                    ) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = "Search cancel"
                        )
                    }
                },
                value = text,
                onValueChange = {
                    text = it
                },
                label = {
                    Text(stringResource(R.string.todo_form_text_label))
                },
                modifier = Modifier.fillMaxWidth().padding(end = 15.dp)
            )
        },

        )


}

@Preview
@Composable
fun SearchTopBarPreview() {
    SearchTopBar(
        onCancelClick = {}
    )
}