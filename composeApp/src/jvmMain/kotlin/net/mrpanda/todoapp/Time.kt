package net.mrpanda.todoapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class Time {

    var time by mutableStateOf(currentTime())
    private set
    var date by mutableStateOf(currentDate())
    private set

    private fun currentDate(): String {
        val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${date.day}.${date.month.number}.${date.year}"
    }

    private fun currentTime(): String {
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return "${time.hour}:${time.minute.toString().padStart(2, '0')}"
    }
}

