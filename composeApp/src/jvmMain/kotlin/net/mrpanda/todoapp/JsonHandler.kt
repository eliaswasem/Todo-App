package net.mrpanda.todoapp

import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.buffered
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class TodoEntry(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: String,
    val modifiedAt: String,
    val completed: Boolean
)

class JsonHandler {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val time = Time()
    private fun now() = "${time.date} ${time.time}"

    private val saveDir  = Path(getHomeDir(), ".todoapp")
    private val savePath = Path(saveDir.toString(), "todos.json")

    fun save(entries: List<TodoEntry>) {
        SystemFileSystem.createDirectories(saveDir)
        SystemFileSystem.sink(savePath).buffered().use { it.writeString(json.encodeToString(entries)) }
    }

    fun load(): List<TodoEntry> {
        if (!SystemFileSystem.exists(savePath)) return emptyList()
        val text = SystemFileSystem.source(savePath).buffered().use { it.readString() }
        return json.decodeFromString<List<TodoEntry>>(text)
    }

    fun getTodos(
        entries: List<TodoEntry>,
        amount: Int = Int.MAX_VALUE,
        sortFrom: String = "newest"
    ): List<TodoEntry> {
        val sorted = when (sortFrom) {
            "newest" -> entries.sortedByDescending { it.createdAt }
            "oldest" -> entries.sortedBy { it.createdAt }
            else -> entries
        }
        return sorted.take(amount)
    }

    fun getTodo(entries: List<TodoEntry>, id: Int): TodoEntry? {
        return entries.find { it.id == id }
    }

    fun addTodo(entries: List<TodoEntry>, title: String, description: String): List<TodoEntry> {
        val nextId = (entries.maxOfOrNull { it.id } ?: 0) + 1
        return (entries + TodoEntry(
            id          = nextId,
            title       = title,
            description = description,
            createdAt   = now(),
            modifiedAt  = now(),
            completed   = false
        )).also { save(it) }
    }

    fun deleteTodo(entries: List<TodoEntry>, id: Int): List<TodoEntry> {
        require(entries.any { it.id == id }) { "Todo with id=$id not found" }
        return entries.filter { it.id != id }.also { save(it) }
    }

    fun updateTodo(
        entries: List<TodoEntry>,
        id: Int,
        title: String?       = null,
        description: String? = null,
        completed: Boolean?  = null
    ): List<TodoEntry> {
        require(entries.any { it.id == id }) { "Todo with id=$id not found" }
        return entries.map { entry ->
            if (entry.id != id) entry
            else entry.copy(
                title       = title       ?: entry.title,
                description = description ?: entry.description,
                completed   = completed   ?: entry.completed,
                modifiedAt  = now()
            )
        }.also { save(it) }
    }
}