package ru.baderik.todo_app.presentaion.taskdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.baderik.todo_app.databinding.FragmentTaskDetailBinding
import java.util.UUID

class TaskDetailFragment : Fragment() {

    private lateinit var binding: FragmentTaskDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false)

        return binding.root
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