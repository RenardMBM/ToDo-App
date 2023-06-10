package ru.baderik.todo_app.presentaion.tasks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.baderik.todo_app.model.task.Task

class TasksViewModel : ViewModel() {

    private val allTasks = MutableLiveData<List<Task>>()
    private val favoriteTasks = MutableLiveData<List<Task>>()
    private val completedTasks = MutableLiveData<List<Task>>()

    fun createTask(task: Task) = viewModelScope.launch {
        Log.d("tasks", "Tasks created\n$task")
    }

}