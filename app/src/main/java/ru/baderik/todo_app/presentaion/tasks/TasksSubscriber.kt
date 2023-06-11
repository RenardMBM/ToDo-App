package ru.baderik.todo_app.presentaion.tasks

interface TasksSubscriber {

    fun observeData(adapter: TasksAdapter)

}