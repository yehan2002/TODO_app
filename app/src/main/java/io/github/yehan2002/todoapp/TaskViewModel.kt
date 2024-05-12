package io.github.yehan2002.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.yehan2002.todoapp.database.entities.Task

class TaskViewModel: ViewModel() {
    private val mutableTasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = mutableTasks

    fun setTasks(tasks: List<Task>){
        mutableTasks.value = tasks
    }
}