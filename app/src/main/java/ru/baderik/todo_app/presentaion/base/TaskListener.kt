package ru.baderik.todo_app.presentaion.base

import ru.baderik.todo_app.model.task.Task
import java.util.UUID

interface TaskListener {

    fun onTaskPressed(id: UUID)

    fun addTaskPressed()

    fun changeTask(task: Task)

    fun deleteTasK(task: Task)

}