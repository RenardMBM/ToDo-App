package ru.baderik.todo_app.presentaion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.ItemColumnBinding
import ru.baderik.todo_app.databinding.ItemTaskBinding
import ru.baderik.todo_app.model.task.Task

class TasksAdapter : Adapter<TasksAdapter.TasksViewHolder>() {

    private val columns = listOf<Task>(
        Task("title1", ""),
        Task("title2", ""),
        Task("title3", ""),
        Task("title4", ""),
        Task("title5", ""),
        )

    class TasksViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemTaskBinding.bind(view)

        fun bind(task: Task) = with(binding) {

            titleTextView.text = task.title
            additionalInfoImage.visibility = if (task.additionalInfo.isNotBlank()) View.VISIBLE else View.GONE

        }
    }

    override fun getItemCount(): Int = columns.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_column, parent, false)

        return TasksViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(columns[position])
    }
}