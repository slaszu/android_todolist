package pl.slaszu.todoapp.domain

import javax.inject.Inject

class PresentationService @Inject constructor() {
    fun process(todoList: List<TodoModel>, setting: Setting): List<TodoModel> {
        return if (setting.showDone) {
            todoList
        } else {
            todoList.filter { !it.done }
        }
    }
}