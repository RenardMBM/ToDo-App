package ru.baderik.todo_app.presentaion.base

import ru.baderik.todo_app.model.task.Task
import java.util.UUID

interface TaskListener {

    fun onTaskPressed(id: UUID)

    fun onTaskChanged(task: Task)

    fun onTaskDeleted(task: Task)

}