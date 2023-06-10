package ru.baderik.todo_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.baderik.todo_app.database.tasks.TaskDao
import ru.baderik.todo_app.model.task.Task

@Database(entities = [ Task::class ], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}