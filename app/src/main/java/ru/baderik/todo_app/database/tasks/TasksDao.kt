package ru.baderik.todo_app.database.tasks

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.baderik.todo_app.model.task.Task
import java.util.UUID

@Dao
interface TaskDao {

    @Query("select * from task where isCompleted = 0")
    fun getTasks(): LiveData<List<Task>>

    @Query("select * from task where isFavorite = 1 and isCompleted = 0")
    fun getFavoriteTasks(): LiveData<List<Task>>

    @Query("select * from task where isCompleted = 1")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("select * from task where id=(:id)")
    fun getTaskById(id: UUID): LiveData<Task?>

    @Insert
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}