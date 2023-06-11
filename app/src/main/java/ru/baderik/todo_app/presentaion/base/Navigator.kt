package ru.baderik.todo_app.presentaion.base

import androidx.fragment.app.Fragment

interface Navigator {

    fun launch(f: Fragment)

    fun goBack()

}