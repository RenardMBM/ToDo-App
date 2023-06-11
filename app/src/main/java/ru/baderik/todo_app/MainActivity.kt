package ru.baderik.todo_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.baderik.todo_app.databinding.ActivityMainBinding
import ru.baderik.todo_app.presentaion.base.Navigator
import ru.baderik.todo_app.presentaion.tasks.TasksFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            launchFragment(f = TasksFragment.newInstance(), addToBackStack = false)
    }

    override fun launch(f: Fragment) = launchFragment(f = f)

    private fun launchFragment(f: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .replace(R.id.fragmentHolder, f)
            .commit()
    }

    @Suppress("DEPRECATION")
    override fun goBack() {
        onBackPressed()
    }
}