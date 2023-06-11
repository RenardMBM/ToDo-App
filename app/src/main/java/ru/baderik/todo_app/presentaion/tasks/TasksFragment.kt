package ru.baderik.todo_app.presentaion.tasks

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.DialogAddTaskBinding
import ru.baderik.todo_app.databinding.FragmentTasksBinding
import ru.baderik.todo_app.model.task.Task
import ru.baderik.todo_app.presentaion.base.Navigator
import ru.baderik.todo_app.presentaion.base.TaskListener
import ru.baderik.todo_app.presentaion.taskdetail.TaskDetailFragment
import java.util.UUID

class TasksFragment : Fragment(), TasksSubscriber, TaskListener {

    private lateinit var binding: FragmentTasksBinding
    private val adapter = ColumnAdapter(this, this)
    private lateinit var addTaskDialog: BottomSheetDialog
    private val viewModel: TasksViewModel by activityViewModels()
    private var navigator: Navigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        initRecyclerView()

        addTaskDialog = BottomSheetDialog(requireContext(), R.style.BottomDialogStyle)
        addTaskDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        initAddTaskDialog()

        binding.addTaskButton.setOnClickListener {
            addTaskDialog.show()
        }

        return binding.root
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        binding.columnRecyclerView.layoutManager = layoutManager
        binding.columnRecyclerView.adapter = adapter
        snapHelper.attachToRecyclerView(binding.columnRecyclerView)
    }

    private fun initAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        dialogBinding.createTaskButton.isEnabled = false

        dialogBinding.additionalInfoButton.setOnClickListener {
            dialogBinding.additionalExitText.visibility = View.VISIBLE
        }
        dialogBinding.favouriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            dialogBinding.favouriteCheckBox.setButtonDrawable(
                if (isChecked) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        }

        dialogBinding.titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                dialogBinding.createTaskButton.isEnabled = s?.isNotBlank() == true
            }
        })

        dialogBinding.createTaskButton.setOnClickListener {
            Toast.makeText(requireContext(), "Card added", Toast.LENGTH_SHORT).show()
            val task = Task(
                title = dialogBinding.titleEditText.text.toString(),
                additionalInfo = dialogBinding.additionalExitText.text.toString(),
                isFavorite = dialogBinding.favouriteCheckBox.isChecked,
                isCompleted = false,
                parent = null
            )
            viewModel.createTask(task)
            addTaskDialog.dismiss()
        }
        dialogBinding.titleEditText.requestFocus()

        addTaskDialog.setOnDismissListener {
            dialogBinding.apply {
                titleEditText.text.clear()
                additionalExitText.text.clear()
                additionalExitText.visibility = View.GONE
                favouriteCheckBox.isChecked = false
            }
        }

        addTaskDialog.setContentView(dialogBinding.root)
    }

    override fun observeData(adapter: TasksAdapter) {
        when(adapter.type) {
            ColumnAdapter.ALL_TASKS -> viewModel.allTasks.observe(viewLifecycleOwner) {
                adapter.setTasks(it)
            }
            ColumnAdapter.FAVOURITE_TASKS -> viewModel.favoriteTasks.observe(viewLifecycleOwner) {
                adapter.setTasks(it)
            }
            ColumnAdapter.COMPLETED_TASKS -> viewModel.completedTasks.observe(viewLifecycleOwner) {
                adapter.setTasks(it)
            }
        }
    }

    private fun showPopupMenu(view: View, task: Task) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.task_actions_menu)

        popupMenu.menu.getItem(0).title = if (!task.isCompleted)
            "Add to completed"
        else
            "Remove from completed"

        popupMenu.menu.getItem(1).title = if (!task.isFavorite)
            "Add to favorites"
        else
            "Remove from favorites"

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.markAsCompleted -> {
                    task.isCompleted = !task.isCompleted
                    viewModel.updateTask(task)
                }
                R.id.markAsFavorite -> {
                    task.isFavorite = !task.isFavorite
                    viewModel.updateTask(task)
                }
                R.id.deleteTask -> {
                    viewModel.deleteTask(task)
                }
            }
            true
        }

        popupMenu.show()
    }

    override fun onTaskPressed(id: UUID) {
        val fragment = TaskDetailFragment.newInstance(id)
        navigator?.launch(fragment)
    }

    override fun addTaskPressed() {
        addTaskDialog.show()
    }

    override fun onTaskLongPressed(view: View, task: Task) {
        showPopupMenu(view, task)
    }

    override fun changeTask(task: Task) {}

    override fun deleteTasK(task: Task) {}

    companion object {

        fun newInstance(): TasksFragment = TasksFragment()
    }
}