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
    val subtasks: LiveData<List<Task>> = taskId.switchMap { taskId ->
        taskRepository.getSubtasks(taskId)
    }

    fun load(_taskId: UUID) {
        taskId.value = _taskId
    }

    fun saveTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTask(task)
    }

    fun changeSubtask(subtask: Task) = viewModelScope.launch(Dispatchers.IO) {
        val task = this@TaskDetailViewModel.task.value ?: return@launch
        if (subtask.isCompleted) {
            task.completedSubtasksCount += 1
        } else {
            task.completedSubtasksCount -= 1
            if (task.completedSubtasksCount < 0)
                task.completedSubtasksCount = 0
        }
        taskRepository.updateTask(subtask)
        taskRepository.updateTask(task)
    }

    fun addSubtask(subtask: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.addTask(subtask)
        val task = this@TaskDetailViewModel.task.value ?: return@launch
        task.subtaskCount += 1
        taskRepository.updateTask(task)
    }

    fun deleteTask(subtask: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.removeTask(subtask)
        val task = this@TaskDetailViewModel.task.value ?: return@launch
        if (subtask.parent == task.id) {
            task.subtaskCount -= 1
            if (task.subtaskCount < 0) task.subtaskCount = 0
            taskRepository.updateTask(task)
        }

    }
}