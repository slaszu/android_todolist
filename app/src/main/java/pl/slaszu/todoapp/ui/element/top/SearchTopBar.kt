package pl.slaszu.todoapp.ui.element.top

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.slaszu.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    text: String?,
    onChange: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    // Automatyczne ustawienie focusu po wejściu w tryb wyszukiwania
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    CenterAlignedTopAppBar(
        modifier = Modifier.padding(horizontal = 8.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            TextField(
                value = text ?: "",
                onValueChange = onChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = stringResource(R.string.todo_form_text_label),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onCancelClick) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Anuluj wyszukiwanie",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp), // Kształt kapsułki
                colors = TextFieldDefaults.colors(
                    // Usuwamy wszystkie tła i linie, aby TextField "stopił się" z tłem
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    )
}

@Preview
@Composable
fun SearchTopBarPreview() {
    MaterialTheme {
        SearchTopBar(
            onCancelClick = {},
            onChange = {},
            text = "Szukane zadanie..."
        )
    }
}