package ru.baderik.todo_app

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.baderik.todo_app.databinding.ActivityMainBinding
import ru.baderik.todo_app.presentaion.TasksFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add( R.id.fragmentHolder, TasksFragment.newInstance())
                .commit()
        }
    }
}