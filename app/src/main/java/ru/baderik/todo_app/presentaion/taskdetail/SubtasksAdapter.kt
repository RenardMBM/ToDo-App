package ru.baderik.todo_app.presentaion.taskdetail

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.ItemSubtaskBinding
import ru.baderik.todo_app.databinding.ItemTaskBinding
import ru.baderik.todo_app.model.task.Task
import ru.baderik.todo_app.presentaion.base.TaskListener

class SubtasksAdapter(private val listener: TaskListener) : Adapter<SubtasksAdapter.TasksViewHolder>() {

    private var tasks: MutableList<Task> = mutableListOf<Task>()

    class TasksViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemSubtaskBinding.bind(view)

        fun bind(task: Task, listener: TaskListener) = with(binding) {

            titleTextView.text = task.title

            titleTextView.paintFlags = if (task.isCompleted) titleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else 0


            if (task.additionalInfo.isNotBlank()) {
                additionalInfoTextView.visibility = View.VISIBLE
                additionalInfoTextView.text = task.additionalInfo
                additionalInfoTextView.paintFlags = if (task.isCompleted) additionalInfoTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else 0
            } else {
                additionalInfoTextView.visibility = View.GONE
            }

            isCompletedCheckBox.isChecked = task.isCompleted
            isCompletedCheckBox.setOnClickListener {
                task.isCompleted = isCompletedCheckBox.isChecked
                listener.changeTask(task)
            }

            deleteButton.setOnClickListener {
                listener.deleteTasK(task)
            }

        }
    }

    override fun getItemCount(): Int = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subtask, parent, false)

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