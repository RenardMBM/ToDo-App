package ru.baderik.todo_app.presentaion.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.baderik.todo_app.model.task.Task
import ru.baderik.todo_app.model.task.TaskRepository

class TasksViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    private val allTasks: LiveData<List<Task>> = taskRepository.getAllTasks()
    private val favoriteTasks: LiveData<List<Task>> = taskRepository.getFavouriteTasks()
    private val completedTasks: LiveData<List<Task>> = taskRepository.getCompletedTasks()

    fun createTask(task: Task) = viewModelScope.launch {
        taskRepository.addTask(task)
    }

}