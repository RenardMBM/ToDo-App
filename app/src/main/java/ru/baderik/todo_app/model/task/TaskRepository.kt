package ru.baderik.todo_app.model.task

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import ru.baderik.todo_app.database.Database
import java.util.UUID


class TaskRepository private constructor(context: Context) {

    private val database: Database = Room.databaseBuilder(
        context.applicationContext,
        Database::class.java,
        DATABASE_NAME
    ).build()

    private val tasksDao = database.taskDao()

    fun getAllTasks(): LiveData<List<Task>> = tasksDao.getTasks()
    fun getCompletedTasks(): LiveData<List<Task>> = tasksDao.getCompletedTasks()
    fun getFavouriteTasks(): LiveData<List<Task>> = tasksDao.getFavoriteTasks()
    fun getTask(id: UUID): LiveData<Task?> = tasksDao.getTaskById(id)

    suspend fun addTask(task: Task) = tasksDao.addTask(task)

    suspend fun updateTask(task: Task) {

        tasksDao.updateTask(task)

    }

    suspend fun removeTask(task: Task) {

        tasksDao.deleteTask(task)

    }

    companion object {

        private const val DATABASE_NAME = "app-database"
        private const val INITIALIZE_EXCEPTION_MESSAGE = "TaskRepository has not been initialized"

        private var INSTANCE: TaskRepository? = null

        fun get(): TaskRepository {
            return INSTANCE ?: throw IllegalStateException(INITIALIZE_EXCEPTION_MESSAGE)
        }

        fun initialize(context: Context) {

            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

    }

}