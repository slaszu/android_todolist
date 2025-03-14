package pl.slaszu.todoapp.ui.element.top

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    onCancelClick: () -> Unit
) {

    var text by rememberSaveable { mutableStateOf("") }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    CenterAlignedTopAppBar(
        modifier = Modifier.padding(5.dp),
        title = {
            TextField(
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
                placeholder = {
                    Text(stringResource(R.string.todo_form_text_label))
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = TextUnit(4f, TextUnitType.Em)
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
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