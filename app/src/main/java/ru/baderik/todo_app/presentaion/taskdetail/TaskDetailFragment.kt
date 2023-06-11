package ru.baderik.todo_app.presentaion.taskdetail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupMenu.OnMenuItemClickListener
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.baderik.todo_app.R
import ru.baderik.todo_app.databinding.DialogAddTaskBinding
import ru.baderik.todo_app.databinding.FragmentTaskDetailBinding
import ru.baderik.todo_app.model.task.Task
import ru.baderik.todo_app.presentaion.base.Navigator
import ru.baderik.todo_app.presentaion.base.TaskListener
import ru.baderik.todo_app.presentaion.tasks.TasksAdapter
import java.util.UUID

class TaskDetailFragment : Fragment(), TaskListener {

    private lateinit var binding: FragmentTaskDetailBinding
    private val viewModel: TaskDetailViewModel by activityViewModels()
    private val adapter: SubtasksAdapter = SubtasksAdapter(this)
    private var navigator: Navigator? = null
    private lateinit var addSubtaskDialog: BottomSheetDialog
    private lateinit var task: Task

    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) { task.title = s.toString() }
    }

    private val additionalInfoTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) { task.additionalInfo = s.toString() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = requireArguments().getSerializable(ARGS) as UUID
        viewModel.load(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            viewModel.saveTask(task)
            navigator?.goBack()
        }

        binding.moreButton.setOnClickListener {
            showPopupMenu(it)
        }

        addSubtaskDialog = BottomSheetDialog(requireContext(), R.style.BottomDialogStyle)
        addSubtaskDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        initAddTaskDialog()

        binding.favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.favoriteCheckBox.setButtonDrawable(
                if (isChecked) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            task.isFavorite = !task.isFavorite
        }

        binding.addSubtaskImageButton.setOnClickListener {
            addSubtaskDialog.show()
        }

        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.task.observe(viewLifecycleOwner) {task ->
            task?.let {
                this.task = it
                changeScreen()
            }
        }
        viewModel.subtasks.observe(viewLifecycleOwner) {
            adapter.setTasks(it)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.titleEditText.addTextChangedListener(titleTextWatcher)
        binding.additionalInfoExitText.addTextChangedListener(additionalInfoTextWatcher)
    }

    override fun onStop() {
        super.onStop()
        binding.titleEditText.removeTextChangedListener(titleTextWatcher)
        binding.additionalInfoExitText.removeTextChangedListener(additionalInfoTextWatcher)
    }

    private fun initRecyclerView() = with(binding) {
        subtasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        subtasksRecyclerView.adapter = adapter
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.task_detail_menu)

        popupMenu.menu.getItem(0).title = if (!task.isCompleted)
                "Add to completed"
            else
                "Remove from completed"

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.delete -> {
                    viewModel.deleteTask(task)
                    navigator?.goBack()
                }
                R.id.addToCompleted -> {
                    task.isCompleted = !task.isCompleted
                    viewModel.saveTask(task)
                    if (task.isCompleted) navigator?.goBack()
                }
            }
            true
        }

        popupMenu.show()
    }

    private fun changeScreen() = with(binding) {
        titleEditText.setText(task.title)
        additionalInfoExitText.setText(task.additionalInfo)
        favoriteCheckBox.apply {
            isChecked = task.isFavorite
            setButtonDrawable(if (isChecked) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
        }
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
                parent = this.task.id
            )
            viewModel.addSubtask(task)
            addSubtaskDialog.dismiss()
        }
        dialogBinding.titleEditText.requestFocus()

        addSubtaskDialog.setOnDismissListener {
            dialogBinding.apply {
                titleEditText.text.clear()
                additionalExitText.text.clear()
                additionalExitText.visibility = View.GONE
                favouriteCheckBox.isChecked = false
            }
        }

        addSubtaskDialog.setContentView(dialogBinding.root)
    }

    override fun onTaskPressed(id: UUID) {}

    override fun onTaskLongPressed(view: View, task: Task) {}

    override fun addTaskPressed() {

    }

    override fun changeTask(task: Task) {
        viewModel.changeSubtask(task)
    }

    override fun deleteTasK(task: Task) {
        viewModel.deleteTask(task)
    }

    companion object {

        const val ARGS = "ru.baderik.todo_app.presentaion.taskdetail.args"

        fun newInstance(id: UUID): TaskDetailFragment {
            val f = TaskDetailFragment()
            f.arguments = bundleOf(ARGS to id)

            return f
        }
    }

}