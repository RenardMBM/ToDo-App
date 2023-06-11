package ru.baderik.todo_app.presentaion.taskdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.baderik.todo_app.model.task.Task
import ru.baderik.todo_app.model.task.TaskRepository
import java.util.UUID

class TaskDetailViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    private val taskId = MutableLiveData<UUID>()
    val task: LiveData<Task?> = taskId.switchMap { taskId ->
        taskRepository.getTask(taskId)
    }

    fun load(_taskId: UUID) {
        taskId.value = _taskId
    }

    fun saveTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
    }

    fun addSubtask(subtask: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.addTask(subtask)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.removeTask(task)
    }
}