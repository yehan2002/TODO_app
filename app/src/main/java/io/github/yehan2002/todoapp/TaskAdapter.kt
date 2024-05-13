package io.github.yehan2002.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import io.github.yehan2002.todoapp.database.TaskDatabase
import io.github.yehan2002.todoapp.database.entities.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

            if (days == 0L){
                holder.taskDate.text = context.getString(R.string.date_today)
            }else if (days < 0L){
                holder.taskDate.text = String.format(context.getString(R.string.date_past), -days)
            }else{
                holder.taskDate.text = String.format(context.getString(R.string.date_future), days)
            }

            val dao = TaskDatabase.getInstance(context).taskDao();

            holder.view.setOnClickListener {
                if (holder.expanderView.visibility == View.GONE){
                    holder.expanderView.visibility = View.VISIBLE
                    holder.taskExpander.setImageResource(R.drawable.expand_up)
                    holder.taskName.maxLines = Int.MAX_VALUE
                }else{
                    holder.expanderView.visibility = View.GONE
                    holder.taskExpander.setImageResource(R.drawable.expand_down)
                    holder.taskName.maxLines = 1
                }
            }

            holder.editBtn.setOnClickListener {
                context.displayDialog(true, task)
            }


            holder.view.findViewById<AppCompatImageButton>(R.id.delBtn).setOnClickListener {
                val builder = AlertDialog.Builder(context)

                builder.setTitle(context.getString(R.string.delete_dialog_title))
                builder.setMessage(context.getString(R.string.delete_dialog_msg))
                
                builder.setPositiveButton("Delete") { d, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.deleteTask(task)
                        val data = dao.getTasks()
                        context.runOnUiThread {
                            viewModel.setTasks(data)
                        }
                    }
                    d.dismiss()
                }
                builder.setNegativeButton("Cancel") { d, _ ->
                    d.dismiss()
                }

                builder.create().show();
            }

        }
        override fun getItemCount() = tasks.size
}
