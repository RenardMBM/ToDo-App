package ru.baderik.todo_app

import android.app.Application
import ru.baderik.todo_app.model.task.TaskRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}