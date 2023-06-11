package ru.baderik.todo_app.presentaion.base

import android.view.View
import ru.baderik.todo_app.model.task.Task
import java.util.UUID

interface TaskListener {

    fun onTaskPressed(id: UUID)

    fun onTaskLongPressed(view: View, task: Task)

    fun addTaskPressed()

    fun changeTask(task: Task)

    fun deleteTasK(task: Task)

}