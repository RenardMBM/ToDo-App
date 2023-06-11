package ru.baderik.todo_app.presentaion.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.ItemColumnBinding
import ru.baderik.todo_app.presentaion.base.TaskListener

class ColumnAdapter(
    private val subscriber: TasksSubscriber,
    private val listener: TaskListener
    ) : Adapter<ColumnAdapter.ColumnViewHolder>() {

    private val columns = listOf<String>(
        ALL_TASKS,
        FAVOURITE_TASKS,
        COMPLETED_TASKS
    )

    class ColumnViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemColumnBinding.bind(view)
        private lateinit var adapter: TasksAdapter

        fun bind(columnName: String, subscriber: TasksSubscriber, listener: TaskListener) {
            binding.columnTitle.text = columnName

            adapter = TasksAdapter(columnName, listener)
            subscriber.observeData(adapter)
            binding.columnRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
            binding.columnRecyclerView.adapter = adapter
            binding.addCardButton.setOnClickListener {
                listener.addTaskPressed()
            }
        }
    }

    override fun getItemCount(): Int = columns.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_column, parent, false)

        return ColumnViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int) {
        holder.bind(columns[position], subscriber, listener)
    }

    companion object {
        const val ALL_TASKS = "All tasks"
        const val FAVOURITE_TASKS = "Favorite"
        const val COMPLETED_TASKS = "completed"
    }
}