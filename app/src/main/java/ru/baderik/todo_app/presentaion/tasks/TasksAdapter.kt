package ru.baderik.todo_app.presentaion.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.ItemTaskBinding
import ru.baderik.todo_app.model.task.Task
import ru.baderik.todo_app.presentaion.base.TaskListener

class TasksAdapter(val type: String, private val listener: TaskListener) : Adapter<TasksAdapter.TasksViewHolder>() {

    private var tasks: MutableList<Task> = mutableListOf<Task>()

    class TasksViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemTaskBinding.bind(view)

        fun bind(task: Task, listener: TaskListener) = with(binding) {

            root.setOnClickListener {
                listener.onTaskPressed(task.id)
            }
            titleTextView.text = task.title
            additionalInfoImage.visibility = if (task.additionalInfo.isNotBlank()) View.VISIBLE else View.GONE

            if (task.subtaskCount > 0) {
                subtasksImage.visibility = View.VISIBLE
                subtaskCountTextView.visibility = View.VISIBLE
                subtaskCountTextView.text = task.subtaskCount.toString()
            } else {
                subtasksImage.visibility = View.GONE
                subtaskCountTextView.visibility = View.GONE
            }

        }
    }

    override fun getItemCount(): Int = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)

        return TasksViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(tasks[position], listener)
    }
    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks.toMutableList()
        notifyDataSetChanged()
    }
}