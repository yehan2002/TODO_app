package io.github.yehan2002.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yehan2002.todoapp.database.entities.Task

class TaskAdapter (
    private val context: MainActivity,
    private val tasks: List<Task>,
    private val viewModel: TaskViewModel,
    ) : RecyclerView.Adapter<TaskViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
            return TaskViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = tasks[position]
            holder.taskName.text = task.name
            holder.taskPriority.text = task.priority.toString()
            holder.taskDate.text = ""
        }
        override fun getItemCount() = tasks.size
}
