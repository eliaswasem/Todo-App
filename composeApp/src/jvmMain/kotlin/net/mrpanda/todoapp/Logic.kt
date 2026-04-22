package net.mrpanda.todoapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Logic {
    var showWelcome by mutableStateOf(true)
    var showCreateTodoMenu by mutableStateOf(false)
    var showAllTodos by mutableStateOf(false)
    var editTodoId by mutableStateOf<Int?>(null)
    var showEditTodoMenu by mutableStateOf(false)

    var previousScreen by mutableStateOf("welcome")

    private val handler = JsonHandler()
    var todos by mutableStateOf(handler.load())

    fun addTodo(title: String, description: String) {
        todos = handler.addTodo(todos, title, description)
    }

    fun deleteTodo(id: Int) {
        todos = handler.deleteTodo(todos, id)
    }

    fun updateTodo(id: Int, title: String? = null, description: String? = null, completed: Boolean? = null) {
        todos = handler.updateTodo(todos, id, title, description, completed)
    }

    fun getTodos(amount: Int = Int.MAX_VALUE, sortFrom: String = "newest"): List<TodoEntry> {
        return handler.getTodos(todos, amount, sortFrom)
    }

    fun getTodo(id: Int): TodoEntry? {
        return handler.getTodo(todos, id)
    }
}