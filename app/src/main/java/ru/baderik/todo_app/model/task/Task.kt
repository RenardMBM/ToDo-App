package ru.baderik.todo_app.model.task

data class Task(
    var title: String,
    var additionalInfo: String,
    var isCompleted: Boolean = false,
    var isFavorite: Boolean = false
)