package io.github.yehan2002.todoapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
            Log.d("test", String.format("onBindViewHolder: %d %d", task.deadline.time, Date().time))
            if (days == 0L){
                holder.taskDate.text = "Today"
            }else if (days < 0L){
                holder.taskDate.text = String.format("%d Days Ago",-days)
            }else{
                holder.taskDate.text = String.format("%d Days", days)
            }
            val dao = TaskDatabase.getInstance(context).taskDao();

            holder.view.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                val inflater = LayoutInflater.from(context)
                val dialogView: View = inflater.inflate(R.layout.view_dialog, null)

                builder.setView(dialogView)
                builder.setTitle("View task")



                dialogView.findViewById<TextView>(R.id.nameViewText).text = task.name;
                dialogView.findViewById<TextView>(R.id.descriptionViewText ).text = task.description;
                dialogView.findViewById<TextView>(R.id.priorityViewText).text = task.priority.toString();
                dialogView.findViewById<TextView>(R.id.deadlineViewText).text = task.deadline.toString();
                builder.setPositiveButton("Close") { dialog, _ ->
                    dialog.cancel()
                }
                builder.setNegativeButton("Edit") { dialog, _ ->
                    context.displayDialog(true, task);
                }

                val alertDialog = builder.create()
                alertDialog.show()

            }

            holder.view.findViewById<AppCompatImageButton>(R.id.delBtn).setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.deleteTask(task)
                    val data = dao.getTasks()
                    context.runOnUiThread {
                        viewModel.setTasks(data)
                    }
                }

            }

        }
        override fun getItemCount() = tasks.size
}
