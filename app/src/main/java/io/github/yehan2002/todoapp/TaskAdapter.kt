package io.github.yehan2002.todoapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.yehan2002.todoapp.database.entities.Task
import java.util.Date
import java.util.concurrent.TimeUnit

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
            val days = TimeUnit.MILLISECONDS.toDays(task.deadline.time- Date().time)
            Log.d("test", String.format("onBindViewHolder: %d %d", task.deadline.time, Date().time))
            if (days == 0L){
                holder.taskDate.text = "Today"
            }else if (days < 0L){
                holder.taskDate.text = String.format("%d Days Ago",-days)
            }else{
                holder.taskDate.text = String.format("%d Days", days)
            }
        }
        override fun getItemCount() = tasks.size
}
