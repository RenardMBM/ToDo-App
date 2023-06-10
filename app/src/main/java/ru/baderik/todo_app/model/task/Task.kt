package ru.baderik.todo_app.model.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Task(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String,
    var additionalInfo: String,
    var isCompleted: Boolean = false,
    var isFavorite: Boolean = false
)