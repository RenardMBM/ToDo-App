package ru.baderik.todo_app.presentaion.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.ItemTaskBinding
import ru.baderik.todo_app.model.task.Task

class TasksAdapter(val type: String) : Adapter<TasksAdapter.TasksViewHolder>() {

    private var tasks: MutableList<Task> = mutableListOf<Task>()

    class TasksViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemTaskBinding.bind(view)

        fun bind(task: Task) = with(binding) {

            titleTextView.text = task.title
            additionalInfoImage.visibility = if (task.additionalInfo.isNotBlank()) View.VISIBLE else View.GONE

        }
    }

    override fun getItemCount(): Int = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)

        return TasksViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(tasks[position])
    }
    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks.toMutableList()
        notifyDataSetChanged()
    }
}