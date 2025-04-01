package pl.slaszu.todoapp.unit.ui

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import pl.slaszu.todoapp.MainDispatcherRule
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.todo.FakeTodoModel
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.todo.TodoModelFactory
import pl.slaszu.todoapp.domain.todo.TodoRepository
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
    fun `save item if is different then loaded one`() {
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

    @Test
    fun `not save item if is the same as loaded one`() {
        runTest {

            // arrange
            val item = FakeTodoModel(text = "item from repo")

            whenever(presentationService.getStringResource(R.string.todo_form_no_change)).thenReturn(
                "no changes"
            )
            whenever(todoRepo.getById(1)).thenReturn(item)
            val callback: (TodoModel) -> Unit = mock()

            val viewModel = FormViewModel(
                todoRepository = todoRepo,
                todoModelFactory = todoFactory,
                presentationService = presentationService
            )

            // act
            viewModel.loadTodoItemToEditForm(1)
            viewModel.save(
                item = item,
                snackbarHostState = snackbarHostState,
                callback = callback
            )

            // assert
            verify(todoRepo, never()).save(any())
            verify(callback, never()).invoke(any())
            verify(snackbarHostState).showSnackbar(
                message = eq("no changes"),
                actionLabel = anyOrNull(),
                withDismissAction = any(),
                duration = any()
            )
        }
    }

    @Test
    fun `create new item if id=0 is loaded`() {
        // arrange
        val item = FakeTodoModel(text = "new item")

        whenever(todoFactory.createDefault()).thenReturn(item)

        val viewModel = FormViewModel(
            todoRepository = todoRepo,
            todoModelFactory = todoFactory,
            presentationService = presentationService
        )

        // act
        viewModel.loadTodoItemToEditForm(0)

        // assert
        Assert.assertEquals(item, viewModel.todoEditModel.value)
        verify(todoFactory).createDefault()

    }

    @Test
    fun `create new item if id not exists`() {
        runTest {

            // arrange
            val item = FakeTodoModel(text = "new item")

            whenever(todoFactory.createDefault()).thenReturn(item)
            whenever(todoRepo.getById(1)).thenReturn(null)

            val viewModel = FormViewModel(
                todoRepository = todoRepo,
                todoModelFactory = todoFactory,
                presentationService = presentationService
            )

            // act
            viewModel.loadTodoItemToEditForm(1)

            // assert
            Assert.assertEquals(item, viewModel.todoEditModel.value)
            verify(todoRepo).getById(1)
            verify(todoFactory).createDefault()
        }
    }

}