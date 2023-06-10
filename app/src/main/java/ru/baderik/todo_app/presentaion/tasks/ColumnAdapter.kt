package ru.baderik.todo_app.presentaion.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.ItemColumnBinding

class ColumnAdapter : Adapter<ColumnAdapter.ColumnViewHolder>() {

    private val columns = listOf<String>(
        "All tasks",
        "Favorite",
        "Completed",
    )

    class ColumnViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemColumnBinding.bind(view)
        private val adapter = TasksAdapter()

        fun bind(columnName: String, pos: Int) {
            binding.columnTitle.text = columnName

            binding.columnRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)

            binding.columnRecyclerView.adapter = adapter
        }
    }

    override fun getItemCount(): Int = columns.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_column, parent, false)

        return ColumnViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int) {
        holder.bind(columns[position], position)
    }
}