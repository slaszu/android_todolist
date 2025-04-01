package pl.slaszu.todoapp.domain.todo

import pl.slaszu.todoapp.R

enum class TodoItemType(val translationResourceKey: Int, val index: Int) {
    TIMELINE(R.string.item_type_timeline, 0),
    GENERAL(R.string.item_type_general, 1),
    DONE(R.string.item_type_done, 2)
}