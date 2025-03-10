package pl.slaszu.todoapp.ui

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.slaszu.todoapp.MainDispatcherRule
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.FakeTodoModel
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoModelFactory
import pl.slaszu.todoapp.domain.TodoRepository
import pl.slaszu.todoapp.ui.view_model.FormViewModel

@RunWith(MockitoJUnitRunner::class)
class FormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var todoRepo: TodoRepository<TodoModel>

    @Mock
    private lateinit var todoFactory: TodoModelFactory<TodoModel>

    @Mock
    private lateinit var presentationService: PresentationService

    @Mock
    private lateinit var snackbarHostState: SnackbarHostState

    @Test
    fun saveWorkCorrect() {
        runTest {

            // arrange
            val item = FakeTodoModel(text = "item to save")
            val itemSaved = item.copy(text = "item saved")

            whenever(presentationService.getStringResource(R.string.todo_form_saved)).thenReturn("saved")
            whenever(todoRepo.save(item)).thenReturn(itemSaved)
            val callback: (TodoModel) -> Unit = mock()

            val viewModel = FormViewModel(
                todoRepository = todoRepo,
                todoModelFactory = todoFactory,
                presentationService = presentationService
            )

            // act
            viewModel.save(
                item = item,
                snackbarHostState = snackbarHostState,
                callback = callback
            )

            // assert
            verify(todoRepo).save(item)
            verify(callback).invoke(itemSaved)
            verify(snackbarHostState).showSnackbar(
                message = eq("saved"),
                actionLabel = anyOrNull(),
                withDismissAction = any(),
                duration = any()
            )
        }
    }

}